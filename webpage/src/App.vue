<template>
  <blog-surface/>
  <div class="layout">
    <blog-menu/>
    <router-view v-slot="{Component}">
      <transition name="fade" mode="out-in">
        <component :is="Component"/>
      </transition>
    </router-view>
    <div class="footer-margin"></div>
    <blog-footer/>
  </div>
</template>

<script>
import BlogMenu from "@/components/BlogMenu";
import BlogLoading from "@/components/BlogLoading";
import BlogFooter from "@/components/BlogFooter";
import {useRoute, useRouter} from "vue-router/dist/vue-router";
import BlogSurface from "@/components/BlogSurface";

export default {
  name: 'App',
  components: {
    BlogSurface,
    BlogFooter,
    BlogLoading,
    BlogMenu
  },
  setup() {
  }
}
</script>

<style scoped>
.layout {
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
