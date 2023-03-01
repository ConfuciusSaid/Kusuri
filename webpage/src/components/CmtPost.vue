<template>
  <div class="wrapper-comment-post-container">
    <div class="wrapper-comment-post">
      <div class="wrapper-avatar">
        <img class="image-avatar" v-if="self" :src="self.avatar"/>
        <div v-else class="empty-avatar" @click="login()">登录</div>
      </div>
      <textarea v-if="self" class="input-comment" placeholder="请发一条友善的评论" v-model="text" type="text"/>
      <div v-else class="input-comment input-disable" @click="login()">
        请登录后再发表评论
      </div>
      <div v-if="self" class="btn-post" @click="post(text)">
        发送
      </div>
      <div v-else class="btn-post" @click="login()">
        登录
      </div>
    </div>
  </div>

</template>

<script>
import {inject, ref} from "vue";
import {useRouter} from "vue-router";
import {useRoute} from "vue-router/dist/vue-router";

export default {
  name: "CmtPost",
  props: ['post'],
  setup() {
    let self = inject("self");
    let text = ref("");
    let router = useRouter();
    let route = useRoute();

    function login() {
      localStorage.setItem("login_target", JSON.stringify({
        name: route.name,
        params: route.params
      }));
      router.push({name: "Login"});
    }

    return {
      self,
      text,
      login
    }
  }
}
</script>

<style scoped>

.wrapper-comment-post-container {
  display: flex;
  flex-direction: column;
}

.wrapper-comment-post {
  margin: 25px 25px 0;
  padding-bottom: 10px;
  flex: 1;
  display: flex;
}

.wrapper-avatar {
  width: 40px;
  height: 40px;
  min-width: 40px;
  border-radius: 100%;
  overflow: hidden;
  margin: 0 5px;
  user-select: none;
}

.image-avatar {
  width: 40px;
  max-height: 40px;
  min-height: 40px;
}

.empty-avatar {
  width: 39px;
  max-height: 39px;
  min-height: 39px;
  font-size: 16px;
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: center;
  border-radius: 100%;
  border: 1px gray solid;
  color: white;
  font-weight: bold;
  font-family: KaiTi,serif;
  background-color: dodgerblue;
  cursor: pointer;
}

.input-comment {
  margin-left: 5px;
  border-radius: 5px;
  width: 100%;
  padding: 10px 10px 10px 10px;
  font-size: 16px;
  height: 40px;
  transition: .3s;
  resize: none;
  background-color: #ececec;
  border: 1px solid #d0d7de;
  outline: dodgerblue;
  user-select: none;
}

.input-comment:focus, .input-comment:focus-visible {
  height: 55px;
  background-color: white;
  border: 1px solid dodgerblue;
  box-shadow: 0 0 3px dodgerblue;
}

.input-disable {
  display: flex;
  flex-direction: column;
  text-align: center;
  color: gray;
  user-select: none;
  cursor: pointer;
}

.btn-post {
  width: 100px;
  background-color: #409eff;
  color: white;
  font-size: 14px;
  border: 1px #409eff solid;
  cursor: pointer;
  user-select: none;
  border-radius: 5px;
  margin-left: 8px;
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.btn-post:hover {
  background-color: #79bbff;
  border: 1px #79bbff solid;
}

.btn-post:active {
  background-color: #337ecc;
  border: 1px #337ecc solid;
}

@media (max-width: 719px) {
  .wrapper-comment-post {
    margin-left: 15px;
    margin-right: 15px;
  }
}

@media (min-width: 1200px) {
  .wrapper-comment-post {
    width: 1100px;
    align-self: center;
  }
}

</style>
