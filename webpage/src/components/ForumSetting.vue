<template>
  <transition name="fade" mode="out-in">
    <blog-loading v-if="loading"/>
    <div v-else-if="self" class="wrapper-setting">
      <div class="setting">
        <div class="setting-title">
          个人签名：
        </div>
        <div class="tags-item-container">
          <tag-editor :length="30" :list="tagsObj" :check="checkTag" name="签名"/>
        </div>
        <div class="setting-title">
          预览图：
        </div>
        <div class="wrapper-motto-container">
          <div class="wrapper-motto">
            <motto-display :motto="tags" color="white" :key="mottoKey"/>
          </div>
        </div>
        <div class="wrapper-btn-right">
          <div class="btn-setting" @click="setMotto()">
            设置
          </div>
        </div>
      </div>
    </div>
    <div v-else>
      <error-page :code="403" text="用户未登录"/>
    </div>
  </transition>
</template>

<script>
import TagEditor from "@/components/TagEditor";
import MsgApi from "@/js/message";
import {inject, ref, watch} from "vue";
import BlogLoading from "@/components/BlogLoading";
import MottoDisplay from "@/components/MottoDisplay";
import api from "@/js/api";
import ErrorPage from "@/components/ErrorPage";

export default {
  name: "ForumSetting",
  components: {ErrorPage, MottoDisplay, BlogLoading, TagEditor},
  setup() {
    let tags = ref([]);
    let tagsObj = {data: tags};
    let loading = ref(true);

    let self = inject("self");
    console.log(self)
    if (self.value && self.value.motto) {
      let index = 0;
      for (let tag of self.value.motto.split(" ")) {
        tags.value.push({name: tag, time: index++});
      }
    }

    function checkTag(tag) {
      if (!tag.match(/^[\u4e00-\u9fa5，]{1,12}$/)) {
        MsgApi.error("每条签名应由1~12位汉字或汉语的逗号组成");
        return false;
      }
      return true;
    }

    setTimeout(() => loading.value = false, 100);

    let mottoKey = ref(0);
    watch(tags.value, () => {
      mottoKey.value++;
    })

    function setMotto() {
      let motto = "";
      let first = true;
      for (let tag of tags.value) {
        if (first) {
          motto += (tag.name);
          first = false;
        } else {
          motto += (" " + tag.name);
        }
      }

      if (mottoKey.value === 0) {
        return;
      }
      api.setMotto(motto).then(resp => {
        if (resp.success) {
          MsgApi.success("修改签名成功");
        }
      })
    }

    return {
      tagsObj,
      tags,
      checkTag,
      mottoKey,
      setMotto,
      self
    }
  }
}
</script>

<style scoped>

.fade-enter-active, .fade-leave-active {
  transition: .4s;
}

.fade-enter-from, .fade-leave-to {
  opacity: 0;
}

.wrapper-motto-container {
  position: relative;
  margin: 10px 0 10px -50px;
  height: 36px;
}

.wrapper-motto {
  position: absolute;
  width: 100%;
  /*top: 75vh;*/
  left: 50vw;
}

.setting {
  border: 1px white solid;
  background-color: #fdfdfd;
  border-radius: 5px;
  box-shadow: 0 0 5px gray;
  padding: 15px 25px;
  overflow: hidden;
}

.wrapper-setting {
  display: flex;
  flex-direction: column;
  justify-content: center;
  border: 1px white solid;
  border-radius: 5px;
  margin: 10px 25px 0;
  padding-bottom: 10px;
}

.setting-title {
  color: royalblue;
  text-shadow: 1px 1px 2px dodgerblue;
  margin: 10px 0;
  font-size: 28px;
  font-weight: bold;
  font-family: KaiTi, serif;
}

.wrapper-btn-right {
  display: flex;
  justify-content: flex-end;
}

.btn-setting {
  width: 100px;
  height: 32px;
  background-color: #409eff;
  color: white;
  font-size: 14px;
  border: 1px #409eff solid;
  cursor: pointer;
  user-select: none;
  border-radius: 5px;
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.btn-setting:hover {
  background-color: #79bbff;
  border: 1px #79bbff solid;
}

.btn-setting:active {
  background-color: #337ecc;
  border: 1px #337ecc solid;
}

@media (max-width: 719px) {
  .wrapper-setting {
    margin-left: 15px;
    margin-right: 15px;
  }

  .wrapper-motto-container {
    position: relative;
    margin: 10px 0 10px -25px;
    height: 36px;
  }

  .wrapper-motto-container {
    position: relative;
    margin: 10px 0 10px -40px;
  }
}

@media (min-width: 1200px) {
  .wrapper-setting {
    width: 1100px;
    align-self: center;
  }
}
</style>
