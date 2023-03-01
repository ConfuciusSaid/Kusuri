<template>
  <div>
    <div v-if="isAdmin">
      <router-view/>
    </div>
    <div v-else>
      <error-page text="未具备管理员权限" :code="403"/>
    </div>
  </div>
</template>

<script>
import api from "@/js/api";
import ErrorPage from "@/components/ErrorPage";
import {ref} from "vue";

export default {
  name: "BlogManage",
  components: {ErrorPage},
  setup() {
    let isAdmin = ref(false);

    api.checkToken().then(resp => {
      if (resp.data !== null) {
        api.checkAdmin(resp.data.id).then(resp => {
          isAdmin.value = resp.data;
        }).catch(e => e);
      }
    }).catch(e => e);
    return {
      isAdmin
    }
  }
}
</script>

<style scoped>

</style>
