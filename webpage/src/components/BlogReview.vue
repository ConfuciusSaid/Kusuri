<template>
  <transition name="fade" mode="out-in">
    <div v-if="!user.self">
      <error-page text="权限不足" :code="403"/>
    </div>
    <blog-loading v-else-if="loading"/>
    <div v-else class="wrapper-list">
      <blog-list :blog-list="reviewList"
                 :buttons="buttons"
                 :show-review="true"
                 :page="reviewList.length?null:{records:[]}"
      />
    </div>
  </transition>
</template>

<script>
import BlogLoading from "@/components/BlogLoading";
import ErrorPage from "@/components/ErrorPage";
import {inject, ref} from "vue";
import api from "@/js/api";
import BlogList from "@/components/BlogList";
import MsgApi from "@/js/message";
import {useRouter} from "vue-router";
import {useRoute} from "vue-router/dist/vue-router";

export default {
  name: "BlogReview",
  components: {BlogList, ErrorPage, BlogLoading},
  setup() {
    let loading = ref(false);
    let reviewList = ref([]);
    let user = inject("user");
    let router = useRouter();
    let route = useRoute();

    function load() {
      loading.value = true;
      api.getReview().then(resp => {
        if (resp.success) {
          reviewList.value = resp.data;
          loading.value = false;
        }
      })
    }

    load();

    let buttons = [{
      className: "icon-redo2",
      click(blog) {
        MsgApi.confirm("请确认", "是否把该博客退回为草稿？(会覆盖，操作不可逆)", state => {
          if (state === 1 || state === 2) {
            api.cancelReview(blog.id).then(resp => {
              if (resp.success) {
                MsgApi.success("成功退回");
                if (state === 2) {
                  localStorage.setItem("blog_title", blog.title);
                  localStorage.setItem("blog_content", blog.content);
                  let tags = [];
                  let index = 0;
                  for (let tag of JSON.parse(blog.tags)) {
                    tags.push({name: tag, time: index++});
                  }
                  localStorage.setItem("blog_tags", JSON.stringify(tags));
                  router.push({name: "BlogUpload", params: route.params})
                } else {
                  load();
                }
              }
            })
          }
        }, ["仅退回", "是", "否"])
      }
    }]

    return {
      user,
      loading,
      reviewList,
      buttons
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

</style>
