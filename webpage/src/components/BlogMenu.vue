<template>
  <div class="wrapper1">
    <a v-if="menu.user.value" class="icon-avatar"
       @click="menu.avatarClick()">
      <!--       @click="$router.push({name:'UserSurface'})">-->
      <img class="image-avatar" :src="menu.user.value.avatar??''"/>
    </a>
    <div v-else class="wrapper-empty-avatar">
      <div class="empty-avatar" @click="login()">登录</div>
    </div>
    <a v-if="menu.user.value" class="name"
    :href="'https://space.bilibili.com/'+menu.user.value.uid"
    target="_blank">
      {{ menu.user.value.name ?? '' }}
    </a>
    <div v-else class="name">
    </div>
    <div class="can-hide">
      <router-link v-for="item in menu.list"
                   :class="item.className + ' wrapper-link'"
                   :active-class="item.className + ' wrapper-link active'"
                   :to="item.to">
        <div class="menu-static-text">{{ item.text1 }}</div>
      </router-link>
    </div>
    <div class="wrapper-search-box">
      <i class="icon-search"></i>
      <input class="input-search"
             type="text"
             ref="searchBox"
             placeholder autocomplete="off"
             spellcheck="false"
             @keydown.enter="searchEvent()"
             v-model="searchText"
      />
    </div>
    <div class="can-show">
      <div class="wrapper-item">
        <a :class="menuButtonClass" ref="menuButton" @click="showMenu()"></a>
      </div>
    </div>
  </div>
  <transition name="popup">
    <div v-if="isMenuShow" class="wrapper2 can-show">
      <router-link v-for="item in menu.list"
                   :class="item.className + ' wrapper-link'"
                   :active-class="item.className + ' wrapper-link active'"
                   :to="item.to">
        <div class="menu-static-text">{{ item.text2 }}</div>
      </router-link>
    </div>
  </transition>
</template>

<script>
import {inject, onMounted, ref} from "vue";
import {useRoute} from "vue-router/dist/vue-router";
import {useRouter} from "vue-router";

export default {
  name: "BlogMenu",
  props: ['menu'],
  setup() {
    const searchBox = ref(null);
    let searchText = ref("");
    let menuButton = ref(null);
    let menuButtonClass = ref("icon-menu3 ");
    let isMenuShow = ref(false);
    let route = useRoute();
    let router = useRouter();

    function searchEvent() {
      searchBox.value.blur();
      searchText.value = "";
    }

    function showMenu() {
      isMenuShow.value ^= true;
      menuButtonClass.value = "icon-menu3 " + (isMenuShow.value ? "active" : "");
    }

    function login() {
      localStorage.setItem("login_target", JSON.stringify({
        name: route.name,
        params: route.params
      }));
      router.push({name: "Login"});
    }

    return {
      searchEvent,
      searchBox,
      searchText,
      showMenu,
      menuButton,
      isMenuShow,
      menuButtonClass,
      login
    }
  }
}
</script>

<style scoped>

.icon-avatar {
  display: flex;
  width: 40px;
  height: 40px;
  min-width: 40px;
  border-radius: 100%;
  overflow: hidden;
  cursor: pointer;
  border: 2px solid cornflowerblue;
  /*background: linear-gradient(45deg, #85FFBD 0%, #FFFB7D 100%);*/
  transition: .2s;
  margin: auto 15px;
}

.icon-avatar:hover {
  box-shadow: 0 0 1px dodgerblue, 0 0 3px dodgerblue, 0 0 5px dodgerblue;
}

.name {
  margin: auto auto auto 0;
  display: flex;
  transition: .2s;
  cursor: pointer;
  white-space: nowrap;
}

.image-avatar {
  width: 100%;
  max-height: 40px;
}

.wrapper-empty-avatar {

  width: 40px;
  height: 40px;
  border-radius: 100%;
  overflow: hidden;
  cursor: pointer;
  margin: auto 15px;
}

.empty-avatar {
  width: 39px;

  min-width: 39px;
  max-height: 39px;
  min-height: 39px;
  font-size: 16px;
  text-align: center;
  padding-top: 1px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  border-radius: 100%;
  border: 1px gray solid;
  color: white;
  font-weight: bold;
  font-family: KaiTi, serif;
  background-color: dodgerblue;
}

.wrapper1 {
  display: flex;
  position: sticky;
  height: 60px;
  min-height: 60px;
  top: 0;
  z-index: 100;
  background-color: white;
  justify-content: flex-end;
  box-shadow: 0 0 5px gray;
  user-select: none;
}

.wrapper2 {
  display: flex;
  top: 60px;
  position: absolute;
  width: 100%;
  background-color: white;
  flex-direction: column;
  box-shadow: 0 0 5px gray;
  transition: .2s;
  z-index: 99;
}

.popup-enter-active, .popup-leave-active {
  transition: .3s;
}

.popup-enter-from, .popup-leave-to {
  transform: translateY(-100%);
}

.wrapper2 > a {
  padding: 5px;
}

form {
  display: flex;
}

.can-hide {
  display: flex;
}

.can-show {
  display: none;
}

.wrapper-item, .wrapper-link {
  padding-left: 25px;
  padding-right: 25px;
}

.wrapper-item {
  display: flex;
  justify-content: center;
}

.wrapper-link {
  display: flex;
  justify-content: center;
}

.wrapper-link > * {
  align-self: center;
}

span {
  font-size: 20px;
}

a {
  text-decoration: none;
  color: black;
  transition: .3s;
}

a:before {
  align-self: center;
}

a.wrapper-link.active {
  color: white;
  background-color: dodgerblue;
  pointer-events: none;
}


.icon-menu3, .input-search {
  cursor: pointer;
}

.wrapper-search-box {
  display: flex;
  margin: 0 15px 0 15px;
  position: relative;
}

.icon-search {
  display: flex;
  position: absolute;
  top: 0;
  bottom: 0;
  z-index: -1;
  left: .6rem;
  margin: auto;
}

.icon-search::before {
  align-self: center;
}


.input-search {
  align-self: center;
  width: 0;
  height: 30px;
  padding: 0 .5rem 0 2rem;
  border-color: transparent;
  background-color: transparent;
  transition: .2s;
  position: relative;
}

.input-search:focus {
  cursor: text;
  width: 100px;
  border: 1px solid dodgerblue;
  outline: dodgerblue;
  border-radius: 5px;
}

.icon-menu3 {
  align-self: center;
  transition: .4s;
}

.icon-menu3.active {
  color: dodgerblue;
  transform: rotateX(180deg);
}

@media (max-width: 719px) {
  .can-hide {
    display: none;
  }

  .can-show {
    display: flex;
  }
}
</style>
