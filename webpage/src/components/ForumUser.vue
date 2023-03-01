<template>
  <transition name="fade" mode="out-in">
    <blog-loading v-if="loading"/>
    <div v-else style="display: flex;flex-direction: column;justify-content: center">
      <div class="user-title">本站会员</div>
      <div class="user-title-line"/>
      <div class="wrapper-users-wrap">
        <div class="wrapper-users">
          <div v-for="user in userList" class="user-item"
               @click="$router.push({name:'UserSurface',params:{id:user.id}})">
            <div class="icon-avatar">
              <img class="image-avatar" :src="user.avatar">
            </div>
            <div class="user-desc-wrapper">
              <div class="user-name">
                {{ user.name }}
              </div>
              <div :class="'desc-level '+rankCheck(user.level)">
                {{ 'Lv' + user.level }}
              </div>
            </div>
          </div>
        </div>
        </div>
    </div>
  </transition>
</template>

<script>
import {ref} from "vue";
import api from "@/js/api";
import {useRoute} from "vue-router";
import BlogLoading from "@/components/BlogLoading";

export default {
  name: "ForumUser",
  components: {BlogLoading},
  setup() {
    let loading = ref(true);
    let userList = ref(null);
    let page = ref(null);
    let route = useRoute();

    function load(pageNo = 1, limit = 10) {
      loading.value = true;
      api.getAllUsers(pageNo, limit).then(resp => {
        if (resp.success) {
          userList.value = resp.data.records;
          page.value = resp.data;
          loading.value = false;
        }
      })
    }

    load(route.params.pageNo ?? 1);

    function rankCheck(level) {
      if (level <= 8) {
        return "level-1";
      } else if (level <= 16) {
        return "level-2";
      } else if (level <= 24) {
        return "level-3";
      } else if (level === 99) {
        return "level-5";
      } else {
        return "level-4";
      }
    }

    return {
      userList,
      page,
      loading,
      rankCheck
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

.user-title {
  text-align: center;
  margin: 10px 0;
  font-size: 36px;
  font-family: KaiTi, serif;
  font-weight: bold;
  color: royalblue;
  text-shadow: 1px 1px 2px dodgerblue;
  user-select: none;
}

.user-title-line {
  margin: 0 50px 10px;
  border: 1px #d9d9d9 solid;
}

.user-item {
  margin-right: 50px;
  display: flex;
  background: #fee2ff linear-gradient(45deg, #fee2ff 0%, #ccf2ff 52%, #c6ffe0 90%);
  cursor: pointer;
  height: 100px;
  border-radius: 50px;
  margin-bottom: 25px;
}

.icon-avatar {
  margin: 10px 0 0 10px;
  display: flex;
  width: 80px;
  height: 80px;
  min-width: 80px;
  border-radius: 100%;
  overflow: hidden;
  user-select: none;
  cursor: pointer;
}

.image-avatar {
  width: 100%;
  max-height: 80px;
}

.wrapper-users-wrap{
  margin: 0 25px auto;
  padding-bottom: 10px;
}

.wrapper-users {
  flex: 1;
  display: flex;
  position: relative;
  justify-content: center;
  flex-flow: wrap;
  margin-right: -50px;
}

.user-desc-wrapper {
  margin: 0 25px;
}

.user-name {
  color: cornflowerblue;
  text-shadow: 1px 1px 3px dodgerblue;
  margin-top: 14px;
  font-size: 28px;
  margin-right: 15px;
  font-weight: bold;
}

.desc-level {
  color: white;
  font-size: 20px;
  margin-left: 3px;
  user-select: none;
}

.level-1 {
  text-shadow: 1px 0 5px lawngreen, 0 1px 5px lawngreen, -1px 0 5px lawngreen, 0 -1px 5px lawngreen,
  1px 0 5px lawngreen, 0 1px 5px lawngreen, -1px 0 5px lawngreen, 0 -1px 5px lawngreen;
}

.level-2 {
  text-shadow: 1px 0 5px lightskyblue, 0 1px 5px lightskyblue, -1px 0 5px lightskyblue, 0 -1px 5px lightskyblue,
  1px 0 5px lightskyblue, 0 1px 5px lightskyblue, -1px 0 5px lightskyblue, 0 -1px 5px lightskyblue;
}

.level-3 {
  text-shadow: 1px 0 5px mediumpurple, 0 1px 5px mediumpurple, -1px 0 5px mediumpurple, 0 -1px 5px mediumpurple,
  1px 0 5px mediumpurple, 0 1px 5px mediumpurple, -1px 0 5px mediumpurple, 0 -1px 5px mediumpurple;
}

.level-4 {
  text-shadow: 1px 0 5px gold, 0 1px 5px gold, -1px 0 5px gold, 0 -1px 5px gold,
  1px 0 5px gold, 0 1px 5px gold, -1px 0 5px gold, 0 -1px 5px gold;
}

.level-5 {
  text-shadow: 1px 0 5px red, 0 1px 5px red, -1px 0 5px red, 0 -1px 5px red,
  1px 0 5px red, 0 1px 5px red, -1px 0 5px red, 0 -1px 5px red;
}

@media (max-width: 719px) {
  .wrapper-users-wrap {
    margin-left: 15px;
    margin-right: 15px;
  }
  .wrapper-users{
    flex-direction: column;
    margin-right: 0;
  }

  .user-name {
    margin-top: 20px;
    font-size: 24px;
    white-space: nowrap;
  }

  .user-item {
    margin-right: 0;
  }

  .user-title-line {
    margin: 0 30px 10px;
  }
}

@media (min-width: 1200px) {
  .user-title-line {
    width: 1050px;
    align-self: center;
  }

  .wrapper-users-wrap {
    width: 1100px;
    align-self: center;
  }
}

</style>
