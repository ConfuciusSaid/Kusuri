<template>
  <div class="wrapper">
    <div style="margin-top:10px;margin-bottom: 100px">制作中...</div>
    <blog-loading/>
  </div>
</template>

<script>
import BlogLoading from "@/components/BlogLoading";

export default {
  name: "BlogComment",
  components: {BlogLoading}
}
</script>

<style scoped>
.wrapper {
  display: flex;
  flex-direction: column;
  text-align: center;
}
</style>
