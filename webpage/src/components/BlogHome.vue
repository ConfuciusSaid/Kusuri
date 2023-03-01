<template>
  <transition name="fade" mode="out-in">
    <blog-loading v-if="loading"/>
    <div v-else-if="showEditor" class="wrapper-editor">
      <div class="editor-item">
        <blog-editor :text="textObj"
                     :update="true"
                     :finish="finish"
                     :exit="back"
        ></blog-editor>
      </div>
    </div>
    <div v-else class="wrapper-and-page">
      <div class="drawer-btn" @click="showInfo^=true;"
           :style="'transform: rotateY('+(showInfo?'180':'0')+'deg);'">
        ◀&nbsp;◀
      </div>
      <transition name="info">
        <div v-show="showInfo" class="wrapper-info2">
          <div class="wrapper-info2">
            <div v-for="info in infoList" class="info-item">
              <v-md-preview :text="info.markdown"></v-md-preview>
              <div class="wrapper-editor-btn" v-show="user.self">
                <div class="btn-editor icon-wrench" @click="onEdit(info, true)"></div>
                <div class="btn-editor icon-bin" @click="onDelete(info, true)"></div>
              </div>
            </div>
          </div>
        </div>
      </transition>
      <div class="wrapper-home">
        <div class="wrapper-desc">
          <div v-for="desc in descList" class="desc-item" @click="showInfo=false">
            <v-md-preview :text="desc.markdown"></v-md-preview>
            <div class="wrapper-editor-btn" v-show="user.self">
              <div class="btn-editor icon-wrench" @click="onEdit(desc)"></div>
              <div class="btn-editor icon-bin" @click="onDelete(desc)"></div>
            </div>
          </div>
        </div>
        <div class="wrapper-info">
          <div v-for="info in infoList" class="info-item">
            <v-md-preview :text="info.markdown"></v-md-preview>
            <div class="wrapper-editor-btn" v-show="user.self">
              <div class="btn-editor icon-wrench" @click="onEdit(info, true)"></div>
              <div class="btn-editor icon-bin" @click="onDelete(info, true)"></div>
            </div>
          </div>
        </div>
      </div>
      <page-guide :page="page" :flip="onFlip"></page-guide>
    </div>
  </transition>
</template>

<script>
import {inject, reactive, ref} from "vue";
import BlogLoading from "@/components/BlogLoading";
import api from "@/js/api"
import {useRoute, useRouter} from "vue-router";
import PageGuide from "@/components/PageGuide";
import MsgApi from "@/js/message.js";
import BlogEditor from "@/components/BlogEditor";

export default {
  name: "BlogMain",
  components: {BlogEditor, PageGuide, BlogLoading},
  setup() {
    let loading = ref(0)
    let descList = ref([])
    let infoList = ref([])
    let showInfo = ref(false);
    let page = ref(null)
    let id = useRoute().params.id;
    let router = useRouter();
    let route = useRoute();
    let user = inject("user");

    function load(pageNo, limit) {
      loading.value = 2;
      showInfo.value = false;
      if (isNaN(pageNo)) pageNo = 1;

      api.getDescByUserId(id, pageNo, limit).then(resp => {
        descList.value = resp.data.records;
        page.value = resp.data;
        loading.value--;
      })
      api.getInfoByUserId(id).then(resp => {
        infoList.value = resp.data;
        loading.value--;
      })
    }

    function onFlip(pageNo) {
      router.push({name: "UserHome", params: {id, pageNo}})
      load(pageNo, 5);
    }

    load(+route.params.pageNo ?? 1, 5);

    let showEditor = ref(false);
    let editing = ref(null);
    let updateApi;
    let markdown = ref("");
    let textObj = ref({data: markdown});

    function onEdit(desc, info) {
      if (info) {
        updateApi = api.updateInfo;
      } else {
        updateApi = api.updateDesc;
      }
      markdown.value = desc.markdown;

      editing.value = desc;
      loading.value = 1;
      showEditor.value = true;
      setTimeout(() => loading.value = 0, 100);
    }

    function doBack() {
      loading.value = 1;
      showEditor.value = false;
      setTimeout(() => loading.value = 0, 100);
    }

    function back() {
      if (markdown.value === editing.value.markdown) {
        doBack();
      } else {
        MsgApi.confirm("请确认", "您的更改未应用，是否直接退出？", state => {
          if (state === 1) {
            doBack();
          }
          if (state === 3) {
            //text = text.replace(/<[\s\S]+>[\s\S]+<\/[\s\S]+>/g, "");
            markdown.value = api.defend(markdown.value);
            updateApi(editing.value.id, markdown.value).then(resp => {
              if (resp.success) {
                editing.value.markdown = markdown.value;
                MsgApi.success("修改成功");
                doBack();
              }
            })
          }
        }, ['是', '否', '应用后退出'])
      }
    }

    function finish() {
      if (markdown.value === editing.value.markdown) {
        MsgApi.tip("您还没进行修改");
        return;
      }
      MsgApi.confirm("请确认", "是否应用此更改？（该操作不可逆）", state => {
        if (state === 1) {
          markdown.value = api.defend(markdown.value);
          updateApi(editing.value.id, markdown.value).then(resp => {
            if (resp.success) {
              editing.value.markdown = markdown.value;
              MsgApi.success("修改成功");
              doBack();
            }
          })
        }
      }, ['是', '否'])
    }

    function onDelete(desc, info) {
      let deleteApi;
      if (info) {
        deleteApi = api.deleteInfo;
      } else {
        deleteApi = api.deleteDesc;
      }
      MsgApi.confirm("警告", "是否删除此项所有数据？（该操作不可逆）", state => {
        if (state === 1) {
          deleteApi(desc.id).then(resp => {
            if (resp.success) {
              MsgApi.success("删除成功");
              load(+route.params.pageNo ?? 1, 5);
            }
          })
        }
      }, ['是', '否'])
    }

    return {
      loading,
      user,
      descList,
      infoList,
      showInfo,
      page,
      onFlip,
      showEditor,
      onEdit,
      editing,
      finish,
      back,
      onDelete,
      textObj
    }
  }
}
</script>

<style scoped>

.info-enter-active, .info-leave-active {
  transition: .3s;
}

.info-enter-from, .info-leave-to {
  transform: translateX(100%);
}

.fade-enter-active, .fade-leave-active {
  transition: .4s;
}

.fade-enter-from, .fade-leave-to {
  opacity: 0;
}

.wrapper-and-page {
  display: flex;
  flex-direction: column;
}

.wrapper-home {
  display: flex;
  justify-content: center;
  z-index: 3;
}

.wrapper-home > .wrapper-desc {
  display: flex;
  flex-direction: column;
  flex: 3;
  z-index: 3;
}

.wrapper-home > .wrapper-info {
  flex-direction: column;
  width: 250px;
}

.wrapper-info2 {
  display: flex;
  flex-direction: column;
  position: absolute;
  width: 100%;
  background-color: white;
  border: 1px white solid;
  box-shadow: 0 0 5px gray;
  z-index: 4;
}

.wrapper-info2 > .info-item {
  width: 225px;
  margin: 5px auto 0;
  box-shadow: 0 0 0;
  border-radius: 0;
  background-color: white;
}

.desc-item, .info-item {
  border: 1px white solid;
  background-color: #fdfdfd;
  border-radius: 5px;
  box-shadow: 0 0 5px gray;
  margin: 25px 25px 0;
  display: flex;
  position: relative;
}


.info-item {
  margin-left: 0;
}


.drawer-btn {
  display: none;
  color: #409eff;
  border-radius: 100%;
  box-shadow: 0 0 4px gray;
  background-color: white;
  width: 40px;
  min-height: 40px;
  top: 50vh;
  left: -20px;
  text-align: center;
  position: fixed;
  justify-content: center;
  flex-direction: column;
  z-index: 5;
  transition: .2s;
  user-select: none;
}

.drawer-btn:active {
  color: white;
  background-color: #409eff;
}

.wrapper-editor-btn {
  margin-left: auto;
  display: flex;
  flex-direction: column;
  justify-content: end;
  position: absolute;
  color: gray;
  bottom: 0;
  right: 0;
}

.wrapper-editor {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.btn-editor {
  margin-bottom: 10px;
  margin-right: 10px;
  cursor: pointer;
  transition: .4s;
}

.btn-editor:hover {
  color: dodgerblue;
}

.editor-item {
  border: 1px white solid;
  background-color: #fdfdfd;
  border-radius: 5px;
  box-shadow: 0 0 5px gray;
  margin: 25px 25px 0;
  padding-bottom: 10px;
}

@media (max-width: 719px) {
  .wrapper-info {
    display: none;
  }

  .drawer-btn {
    display: flex;
  }

  .desc-item {
    margin-left: 15px;
    margin-right: 15px;
  }

  .btn-editor {
    margin-bottom: 5px;
    margin-right: 5px;
  }

  .editor-item {
    margin-left: 15px;
    margin-right: 15px;
  }
}

@media (min-width: 719px) {
  .wrapper-info {
    display: flex;
  }

  .wrapper-info2 {
    display: none;
  }
}

@media (min-width: 1200px) {
  .wrapper-desc {
    width: 900px;
    max-width: 900px;
  }

  .editor-item {
    width: 1100px;
    align-self: center;
  }
}
</style>
