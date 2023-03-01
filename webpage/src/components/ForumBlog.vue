<template>
  <transition name="fade" mode="out-in">
    <blog-loading v-if="loading"/>
    <div v-else-if="!($route.name==='ForumBlogContent')">
      <blog-list
          :page="page"
          :blog-list="blogList"
          :on-click="openBlog"
          :on-flip="onFlip"
          :show-name="true"
      />
    </div>
    <router-view v-else/>
  </transition>
</template>

<script>
import BlogLoading from "@/components/BlogLoading";
import BlogList from "@/components/BlogList";
import {provide, ref} from "vue";
import {useRoute, useRouter} from "vue-router/dist/vue-router";
import api from "@/js/api";

export default {
  name: "ForumBlog",
  components: {BlogList, BlogLoading},
  setup() {
    let loading = ref(0);
    let blogList = ref([]);
    let page = ref(null);
    let users = [];

    let router = useRouter();
    let route = useRoute();

    function load(pageNo, limit) {
      loading.value = 1;
      api.getAllBlogs(pageNo, limit).then(resp => {
        blogList.value = resp.data.records;
        page.value = resp.data;
        loading.value = 0;
        for (let blog of blogList.value) {
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
      })
    }

    load(useRoute().params.pageNo ?? 1, 5);

    function onFlip(pageNo) {
      router.push({name: "UserBlog", params: {id, pageNo}})
      load(pageNo, 5);
    }

    function openBlog(blog) {
      router.push({name: "ForumBlogContent", params: {blogId: blog.id}})
    }

    function onBack() {
      router.push({name: "ForumBlog", params: route.params})
    }

    provide("onBack", onBack);

    return {
      loading,
      blogList,
      openBlog,
      page,
      onFlip
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
