<template>
  <transition name="fade-all" mode="out-in">
    <div v-if="loading"
         style="display:flex;width: 100%;justify-content: center;height: 100vh;">
      <blog-loading/>
    </div>
    <div v-else-if="isExist">
      <blog-surface/>
      <div class="layout-blog-main">
        <blog-menu :menu="menu"/>
        <router-view v-slot="{Component}">
          <transition name="fade" mode="out-in">
            <component :is="Component"/>
          </transition>
        </router-view>
        <div class="footer-margin"></div>
        <blog-footer/>
      </div>
    </div>
    <div v-else>
      <error-page text="用户页面不存在" code="404"/>
    </div>
  </transition>
</template>

<script>
import BlogMenu from "@/components/BlogMenu";
import BlogLoading from "@/components/BlogLoading";
import BlogFooter from "@/components/BlogFooter";
import BlogSurface from "@/components/BlogSurface";
import api from "@/js/api";
import {useRoute} from "vue-router/dist/vue-router";
import {inject, provide, reactive, ref} from "vue";
import ErrorPage from "@/components/ErrorPage";
import {useRouter} from "vue-router";

export default {
  name: 'BlogUser',
  components: {
    ErrorPage,
    BlogSurface,
    BlogFooter,
    BlogLoading,
    BlogMenu
  },
  setup() {
    let isExist = ref(false);
    let user = ref(null);
    let self = ref(null);
    let loading = ref(0);
    let $route = useRoute();
    let $router = useRouter();
    provide('user', user);
    provide('self', self);
    let menu = {
      user,
      list: [{
        className: 'icon-home',
        to: {name: 'UserHome', params: {id: $route.params.id}},
        text1: "主页",
        text2: " 主 页"
      }, {
        className: 'icon-books',
        to: {name: 'UserBlog', params: {id: $route.params.id}},
        text1: "博客",
        text2: " 博 客"
      }, {
        className: 'icon-bubble',
        to: {name: 'UserComment', params: {id: $route.params.id}},
        text1: "留言",
        text2: " 留 言"
      }],
      avatarClick() {
        //window.open("https://space.bilibili.com/" + user.value.uid);
        $router.push({name: "UserSurface", params: {id: $route.params.id}})
      }
    }
    let selfMenu = [{
      className: 'icon-upload',
      to: {name: 'BlogUpload', params: {id: $route.params.id}},
      text1: "发布",
      text2: " 发 布"
    }, {
      className: 'icon-hour-glass',
      to: {name: 'BlogReview', params: {id: $route.params.id}},
      text1: "审核中",
      text2: " 审 核 中"
    }]

    // if (user.self) {
    //
    // }

    function load() {
      loading.value = 1;
      api.getUserById(useRoute().params.id).then(resp => {
        loading.value = 0;
        isExist.value = resp.success;
        if (!isExist.value) {
          $router.push({name: 'User', params: $route.params});
        }
        user.value = resp.data;
        api.checkToken().then(resp => {
          self.value = resp.data;
          if (resp.data != null && resp.data.id === user.value.id) {
            user.value.self = true;
            menu.list.push(...selfMenu)
          }
        }).catch(e => e)
      })
    }

    load();
    return {
      isExist,
      loading,
      menu
    }
  }
}
</script>

<style scoped>


.fade-all-enter-active, .fade-all-leave-active {
  transition: .4s;
}

.fade-all-enter-from, .fade-all-leave-to {
  opacity: 0;
}

.layout-blog-main {
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

@media (max-width: 719px) {
  .footer-margin {
    height: 25px;
    min-height: 25px;
  }
}

</style>
