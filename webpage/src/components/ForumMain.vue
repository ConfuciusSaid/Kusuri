<template>
  <transition name="fade-all" mode="out-in">
    <div v-if="loading"
         style="display:flex;width: 100%;justify-content: center;height: 100vh;">
      <blog-loading/>
    </div>
    <div v-else class="layout-forum-main">
      <blog-menu :menu="menu"/>
      <router-view v-slot="{Component}">
        <transition name="fade" mode="out-in">
          <component :is="Component"/>
        </transition>
      </router-view>
      <div class="footer-margin"/>
      <blog-footer/>
    </div>
  </transition>
</template>

<script>
import BlogMenu from "@/components/BlogMenu";
import {provide, ref} from "vue";
import {useRoute} from "vue-router/dist/vue-router";
import {useRouter} from "vue-router";
import api from "@/js/api";
import BlogLoading from "@/components/BlogLoading";
import BlogFooter from "@/components/BlogFooter";

export default {
  name: "ForumMain",
  components: {BlogFooter, BlogLoading, BlogMenu},
  setup() {
    let self = ref(null);
    let loading = ref(true);
    let route = useRoute();
    let router = useRouter();
    provide("user", self)
    provide("self", self)

    let menu = {
      user: self,
      list: [{
        className: "icon-users",
        to: {name: 'ForumUser'},
        text1: "用户",
        text2: " 用 户"
      }, {
        className: 'icon-books',
        to: {name: 'ForumBlog'},
        text1: "博客",
        text2: " 博 客"
      }],
      avatarClick() {
        router.push({name: "UserSurface", params: {id: self.value.id}})
      }
    }

    function load() {
      loading.value = true;
      api.checkToken().then(resp => {
        self.value = resp.data;
        if(self.value!=null){
          menu.list.push({
            className: "icon-cogs",
            to: {name: 'ForumSetting'},
            text1: "设置",
            text2: " 设 置"
          })
        }
        loading.value = false;
      }).catch(e => loading.value = false)
    }

    load();

    return {
      menu,
      loading
    }
  }
}
</script>

<style scoped>

.layout-forum-main {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.fade-leave-active {
  transition: .4s;
}

.fade-leave-to {
  opacity: 0;
}

.footer-margin {
  height: 50px;
  min-height: 50px;
}

.fade-all-enter-active, .fade-all-leave-active {
  transition: .4s;
}

.fade-all-enter-from, .fade-all-leave-to {
  opacity: 0;
}

</style>
