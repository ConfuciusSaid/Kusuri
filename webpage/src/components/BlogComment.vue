<template>
  <transition name="fade" mode="out-in">
    <blog-loading v-if="loading"/>
    <div v-else class="wrapper-comment">
      <cmt-post :post="onPost"/>
      <cmt-preview :page="page" :on-flip="onFlip" :on-delete="onDelete"/>
    </div>
  </transition>
</template>

<script>
import BlogLoading from "@/components/BlogLoading";
import {inject, ref} from "vue";
import api from "@/js/api";
import {useRoute} from "vue-router/dist/vue-router";
import PageGuide from "@/components/PageGuide";
import {useRouter} from "vue-router";
import CmtPreview from "@/components/CmtPreview";
import CmtPost from "@/components/CmtPost";
import MsgApi from "@/js/message";

export default {
  name: "BlogComment",
  components: {CmtPost, CmtPreview, PageGuide, BlogLoading},
  setup() {
    let loading = ref(true);
    let id = useRoute().params.id;
    let router = useRouter();
    let route = useRoute();
    let page = ref(null);
    let user = inject("user");

    function load(pageNo, limit = 10) {
      loading.value = true;
      api.getUserComments(id, pageNo, limit).then(resp => {
        if (resp.success) {
          page.value = resp.data;
        }
        loading.value = false;
      })
    }

    load(route.params.pageNo ?? 1);

    function onFlip(pageNo) {
      router.push({name: "UserComment", params: {id, pageNo}})
      load(pageNo, 5);
    }

    function onPost(text) {
      if (text === "") {
        MsgApi.error("请先输入要发送的内容");
        return;
      }
      api.insertUserComment(user.value.id, text).then(resp => {
        if (resp.success) {
          MsgApi.success("评论发送成功");
          load(route.params.pageNo ?? 1);
        }
      })
    }

    function onDelete(comment) {
      MsgApi.confirm("请确认", "是否删除该评论？(操作不可逆)", state => {
        if (state === 1) {
          api.deleteUserComment(comment.id).then(resp => {
            if (resp.success) {
              MsgApi.success("评论删除成功");
              load(route.params.pageNo ?? 1);
            }
          })
        }
      }, ["是", "否"]);
    }

    return {
      loading,
      page,
      onFlip,
      onPost,
      onDelete
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

.wrapper-comment {
  display: flex;
  flex-direction: column;
}

</style>
