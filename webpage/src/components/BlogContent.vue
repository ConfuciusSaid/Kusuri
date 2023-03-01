<template>
  <div class="wrapper-blog-content">
    <transition name="fade" mode="out-in">
      <div v-if="loading"
           style="display:flex;width: 100%;justify-content: center;height: 100vh;">
        <blog-loading/>
      </div>
      <div v-else-if="blogContent" class="content-item-container">
        <div class="content-item">
          <v-md-preview :text="blogContent.markdown"></v-md-preview>
          <div class="open-end-btn" @click="onBack()">
            <div>返回</div>
          </div>
        </div>
        <cmt-post :post="onPost"/>
        <cmt-preview :page="page" :on-flip="onFlip" :on-delete="onDelete"/>
      </div>
      <div v-else>
        <error-page :code="404" text="文章不存在"/>
      </div>
    </transition>
  </div>
</template>

<script>
import api from "@/js/api";
import {inject, ref} from "vue";
import BlogLoading from "@/components/BlogLoading";
import ErrorPage from "@/components/ErrorPage";
import {useRoute, useRouter} from "vue-router";
import CmtPost from "@/components/CmtPost";
import CmtPreview from "@/components/CmtPreview";
import MsgApi from "@/js/message";

export default {
  name: "BlogContent",
  components: {CmtPreview, CmtPost, ErrorPage, BlogLoading},
  setup() {
    let loading = ref(0);
    let blogContent = ref(null);

    let id = useRoute().params.blogId;
    let onBack = inject("onBack");

    let router = useRouter();
    let route = useRoute();

    let page = ref(null);

    let lastPage = 1;
    function loadCmt(pageNo = 1, limit = 3) {
      lastPage = pageNo;
      loading.value++;
      api.getBlogComments(id, pageNo, limit).then(resp => {
        if (resp.success) {
          page.value = resp.data;
        }
        loading.value--;
      })
    }

    function load() {
      loading.value++;
      api.getBlogContentById(id).then(resp => {
        blogContent.value = resp.data;
        loading.value--;
      })
      loadCmt();
    }

    load();

    function onFlip(pageNo) {
      loadCmt(pageNo);
    }

    function onPost(text) {
      if (text === "") {
        MsgApi.error("请先输入要发送的内容");
        return;
      }
      api.insertBlogComment(id, text).then(resp => {
        if (resp.success) {
          MsgApi.success("评论发送成功");
          loadCmt(lastPage)
        }
      })
    }

    function onDelete(comment) {
      MsgApi.confirm("请确认", "是否删除该评论？(操作不可逆)", state => {
        if (state === 1) {
          api.deleteBlogComment(comment.id).then(resp => {
            if (resp.success) {
              MsgApi.success("评论删除成功");
              load(route.params.pageNo ?? 1);
            }
          })
        }
      }, ["是", "否"]);
    }

    return {
      loading,
      blogContent,
      id,
      onBack,
      onPost,
      onFlip,
      onDelete,
      page
    }
  }
}
</script>

<style scoped>

.fade-enter-active, .fade-leave-active {
  transition: .2s;
}

.fade-enter-from, .fade-leave-to {
  opacity: 0;
}

.wrapper-blog-content {
  display: flex;
  justify-content: center;
  flex-direction: column;
}

.content-item-container{
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.content-item {
  border: 1px white solid;
  background-color: #fdfdfd;
  border-radius: 5px;
  box-shadow: 0 0 5px gray;
  margin: 25px 25px 0;
  padding-bottom: 10px;
  flex: 1;
  display: flex;
  flex-direction: column;
  position: relative;
}

.open-end-btn {
  margin: 10px 10px 10px 30px;
  border-radius: 100px;
  box-shadow: 0 0 3px gray;
  text-align: center;
  vertical-align: center;
  width: 45px;
  height: 45px;
  cursor: pointer;
  transition: .2s;
  position: fixed;
  top: 69%;
  right: 15px;
  z-index: 5;
  background-color: white;
  user-select: none;

  display: flex;
  flex-direction: column;
  justify-content: center;
}

.open-end-btn:active {
  background-color: dodgerblue;
  color: white;
}

@media (max-width: 719px) {
  .content-item {
    margin-left: 15px;
    margin-right: 15px;
  }
}

@media (min-width: 1200px) {
  .content-item {
    width: 1100px;
    align-self: center;
  }
}

</style>
