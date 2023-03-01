<template>
  <transition name="fade" mode="out-in">
    <blog-loading v-if="loading"/>
    <div v-else-if="!showContent">
      <blog-list
          :page="page"
          :blog-list="blogList"
          :on-click="openBlog"
          :on-flip="onFlip"
          :show-name="true"
          :show-review="true"
      />
    </div>
    <div v-else class="content-item">
      <div class="open-refuse-btn" @click="refuse()" v-show="currentBlog.status==='Reviewing'">
        <div>拒绝</div>
      </div>
      <div class="open-accept-btn" @click="accept()">
        <div>同意</div>
      </div>
      <div class="open-end-btn" @click="backFromContent()">
        <div>返回</div>
      </div>
      <v-md-preview :text="currentBlog.content"></v-md-preview>
    </div>
  </transition>
</template>

<script>
import {provide, ref} from "vue";
import {useRoute, useRouter} from "vue-router/dist/vue-router";
import api from "@/js/api";
import PageGuide from "@/components/PageGuide";
import BlogLoading from "@/components/BlogLoading";
import MsgApi from "@/js/message";
import BlogList from "@/components/BlogList";

export default {
  name: "BlogAccept",
  components: {BlogList, PageGuide, BlogLoading},
  setup() {
    let loading = ref(0);
    let blogList = ref([]);
    let page = ref(null);
    let router = useRouter();
    let route = useRoute();
    let users = [];

    function load(pageNo, limit) {
      loading.value = 1;
      api.getAllReview(pageNo, limit).then(resp => {
        blogList.value = resp.data.records;
        page.value = resp.data;
        for (let blog of blogList.value) {
          // blog.tag = "";
          // blog.tags = JSON.parse(blog.tags);
          // for (let tag of blog.tags) {
          //   blog.tag += (" " + tag);
          // }
          blog.name = users[blog.userId];
          if (!blog.name) {
            api.getUserById(blog.userId).then(resp => {
              if (resp.success) {
                blog.name = resp.data.name;
                users[blog.userId] = blog.name;
              }
            })
          }
        }
        loading.value = 0;
      })
    }

    load(route.params.pageNo ?? 1, 5);

    function onFlip(pageNo) {
      router.push({name: "UserBlog", params: {pageNo}})
      load(pageNo, 5);
    }

    let showContent = ref(false);
    let currentBlog = ref({});

    function openBlog(blog) {
      loading.value = 1;
      currentBlog.value = blog;
      showContent.value = true;
      setTimeout(() => loading.value = 0, 100);
    }

    function accept() {
      api.acceptBlog(currentBlog.value.id).then(resp => {
        if (resp.success) {
          MsgApi.success("审核通过");
          showContent.value = false;
          load(route.params.pageNo ?? 1, 5);
        }
      })
    }

    function refuse() {
      api.refuseBlog(currentBlog.value.id).then(resp => {
        if (resp.success) {
          MsgApi.success("审核拒绝");
          showContent.value = false;
          currentBlog.value.status = 'Failed';
        }
      })
    }

    function backFromContent() {
      showContent.value = false;
      loading.value = 1;
      setTimeout(() => {
        loading.value = 0;
      }, 100)
    }

    return {
      loading,
      blogList,
      showContent,
      currentBlog,
      openBlog,
      page,
      onFlip,
      backFromContent,
      accept,
      refuse
    }
  }
}
</script>

<style scoped>

.wrapper-and-page {
  display: flex;
  flex-direction: column;
}

.open-end-btn, .open-accept-btn, .open-refuse-btn {
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
  right: 5%;
  z-index: 5;
  background-color: white;
  user-select: none;

  display: flex;
  flex-direction: column;
  justify-content: center;
}

.open-end-btn:active, .open-accept-btn:active {
  background-color: dodgerblue;
  color: white;
}

.open-refuse-btn {
  top: 51%;
}

.open-accept-btn {
  top: 60%;
}

.fade-enter-active, .fade-leave-active {
  transition: .4s;
}

.fade-enter-from, .fade-leave-to {
  opacity: 0;
}

.wrapper {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.desc-item, .content-item {
  border: 1px white solid;
  background-color: #fdfdfd;
  border-radius: 5px;
  box-shadow: 0 0 5px gray;
  margin: 25px 25px 0;
  padding-bottom: 10px;
  flex: 1;
}

.desc-item {
  cursor: pointer;
}

.title-desc {
  margin: 10px 10px 10px 4vw;
}

@media (max-width: 719px) {
  .desc-item, .content-item {
    margin-left: 15px;
    margin-right: 15px;
  }
}

@media (min-width: 1200px) {
  .desc-item, .content-item {
    width: 1100px;
    align-self: center;
  }
}
</style>
