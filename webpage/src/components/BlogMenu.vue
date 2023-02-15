<template>
  <div class="wrapper1">
    <div class="icon-avatar" @click="$router.push('/')">
      <img class="image-avatar" src="http://www.kusuri.world/photos/avatar.png"/>
    </div>
    <div class="can-hide">
      <router-link class="icon-home wrapper-link" active-class="icon-home wrapper-link active" to="/main">
        <div>主页</div>
      </router-link>
      <router-link class="icon-books wrapper-link" active-class="icon-books wrapper-link active" to="/blog">
        <div>博客</div>
      </router-link>
      <router-link class="icon-bubble wrapper-link" active-class="icon-bubble wrapper-link active" to="/comment">
        <div>留言</div>
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
      <router-link class="icon-home wrapper-link" active-class="icon-home wrapper-link active" to="/main">
        <div>&nbsp; 主 页</div>
      </router-link>
      <router-link class="icon-books wrapper-link" active-class="icon-books wrapper-link active" to="/blog">
        <div>&nbsp; 博 客</div>
      </router-link>
      <router-link class="icon-bubble wrapper-link" active-class="icon-bubble wrapper-link active" to="/comment">
        <div>&nbsp; 留 言</div>
      </router-link>
    </div>
  </transition>
</template>

<script>
import {onMounted, ref} from "vue";

export default {
  name: "BlogMenu",
  setup() {
    const searchBox = ref(null);
    let searchText = ref("");
    let menuButton = ref(null);
    let menuButtonClass = ref("icon-menu3 ");
    let isMenuShow = ref(false);

    function searchEvent() {
      searchBox.value.blur();
      searchText.value = "";
    }

    function showMenu() {
      isMenuShow.value ^= true;
      menuButtonClass.value = "icon-menu3 " + (isMenuShow.value ? "active" : "");
    }

    return {
      searchEvent,
      searchBox,
      searchText,
      showMenu,
      menuButton,
      isMenuShow,
      menuButtonClass
    }
  }
}
</script>

<style scoped>

.icon-avatar{
  margin: auto auto auto 15px;
  display: flex;
  width: 40px;
  height: 40px;
  border-radius: 100%;
  overflow: hidden;
  cursor: pointer;
  border: 2px gray solid;
  transition: .2s;
}

.icon-avatar:hover{
  box-shadow: 0 0 3px dodgerblue,0 0 5px dodgerblue,0 0 10px dodgerblue;
}

.image-avatar {
  width: 100%;
  max-height: 40px;
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
}

.wrapper2 {
  display: flex;
  top: 60px;
  position: sticky;
  background-color: white;
  flex-direction: column;
  box-shadow: 0 0 5px gray;
  transition: .4s;
}

.popup-enter-active, .popup-leave-active {
  transition: .5s;
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
  border-color: lightskyblue;
  border-radius: 4px;
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
