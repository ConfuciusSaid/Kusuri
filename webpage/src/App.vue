<template>
  <router-view v-slot="{Component}" :key="$route.params.id">
    <transition name="fade" mode="out-in">
      <component :is="Component"/>
    </transition>
  </router-view>
</template>

<script>
import BlogUser from "@/components/BlogMain";
export default {
  name: 'App',
  components: {
    BlogUser
  },
  setup() {
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
