<template>
  <transition name="fade" mode="out-in">
    <blog-loading v-if="loading"/>
    <div v-else-if="!($route.name==='UserBlogContent')">
      <blog-list
          :page="page"
          :blog-list="blogList"
          :on-click="openBlog"
          :on-flip="onFlip"
          :buttons="buttons"
      />
    </div>
    <router-view v-else-if="exist"/>
    <error-page v-else code="404" text="文章不存在"/>
  </transition>
</template>

<script>
import {inject, provide, ref} from "vue";
import BlogLoading from "@/components/BlogLoading";
import api from "@/js/api";
import {useRoute} from "vue-router/dist/vue-router";
import PageGuide from "@/components/PageGuide";
import {useRouter} from "vue-router";
import MsgApi from "@/js/message";
import BlogList from "@/components/BlogList";
import BlogContent from "@/components/BlogContent";
import ErrorPage from "@/components/ErrorPage";

export default {
  name: "BlogUser",
  components: {ErrorPage, BlogContent, BlogList, PageGuide, BlogLoading},
  setup() {
    let loading = ref(0);
    let blogList = ref([]);
    let page = ref(null);
    let id = useRoute().params.id;
    let user = inject("user");
    let exist = ref(true);

    let router = useRouter();
    let route = useRoute();

    function checkStrict(blogId) {
      exist.value = false;
      for (let blog of blogList.value) {
        if (blogId == blog.id) {
          exist.value = true;
        }
      }
    }

    function load(pageNo, limit) {
      loading.value = 1;
      api.getBlogByUserId(id, pageNo, limit).then(resp => {
        blogList.value = resp.data.records;
        page.value = resp.data;
        loading.value = 0;
        checkStrict(route.params.blogId);
      })
    }

    load(useRoute().params.pageNo ?? 1, 5);

    function onFlip(pageNo) {
      router.push({name: "UserBlog", params: {id, pageNo}})
      load(pageNo, 5);
    }

    function openBlog(blog) {
      exist.value = true;
      router.push({name: "UserBlogContent", params: {...route.params, blogId: blog.id}})
    }

    function onBack() {
      router.push({name: "UserBlog", params: route.params})
    }

    provide("onBack", onBack);

    function onDelete(blog) {
      MsgApi.confirm("警告", "是否删除该博客的所有数据？（该操作不可逆）", state => {
        if (state === 1) {
          api.deleteBlog(blog.id).then(resp => {
            if (resp.success) {
              MsgApi.success("删除成功");
              load(route.params.pageNo ?? 1, 5);
            }
          })
        }
      }, ['是', '否'])
    }

    let buttons = [];
    if (user.value.self) {
      buttons = [{
        className: "icon-bin",
        click: onDelete
      }]
    }

    return {
      loading,
      blogList,
      openBlog,
      page,
      onFlip,
      buttons,
      exist
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
