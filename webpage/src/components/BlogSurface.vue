<template>
  <transition name="finish">
    <div class="start" v-show="$route.name==='UserSurface'">
      <transition name="avatar">
        <a class="surface-avatar"
           v-show="$route.name==='UserSurface'"
           @click="$route.name==='UserSurface'?$router.push({name:'UserHome',params:{pageNo:1}}):true"
        >
          <img class="image-avatar" :src="user.avatar??''"/>
        </a>
      </transition>
      <transition name="close">
        <div class="page-close" v-show="$route.name==='UserSurface'"></div>
      </transition>
      <transition name="back">
        <div class="shadow-wrap" v-show="$route.name==='UserSurface'">
          <div class="page-back"></div>
        </div>
      </transition>
      <transition name="decorate">
        <div v-if="$route.name==='UserSurface'" class="wrapper-motto">
          <motto-display :motto="user.motto" color="#ffdde5"/>
        </div>
      </transition>
    </div>
  </transition>
</template>

<script>
import {computed, inject, onMounted, onUnmounted, reactive, ref, watch} from "vue";
import MottoDisplay from "@/components/MottoDisplay";

export default {
  name: "BlogSurface",
  components: {MottoDisplay},
  setup() {
    let user = inject('user')
    let textList = ['']

    if (user.value.motto) {
      textList = user.value.motto.split(' ');
    }

    let index = 0;
    let text = ref(textList[index]);
    let run = ref(true);

    let transform = ref(0);
    let current = 0;

    let maskClass = ref("");

    let finish, animate;

    finish = function () {
      transform.value -= 4;
      if (transform.value === 0) {
        current = 0;
        index = (index + 1) % textList.length;
        text.value = textList[index];
        setTimeout(animate, 200);
      } else {
        setTimeout(finish, 10);
      }
    }

    animate = function () {
      if (!run.value) return;
      transform.value += 16;
      current++;
      if (current === text.value.length || text.value.length === 0) {
        if (text.value.length === 0) {
          current = 0;
          transform.value = 4;
        }
        maskClass.value = "mask-shine";
        setTimeout(() => {
          maskClass.value = "";
          finish();
        }, 2200)
      } else if (textList[index][current] === "，") {
        text.value = text.value.replace(/，/g, "");
        setTimeout(animate, 500);
      } else {
        setTimeout(animate, 100);
      }
    }

    onMounted(() => {
      if (user.value.motto != null) {
        animate();
      }
    })
    onUnmounted(() => {
      run.value = false;
    })


    return {
      text,
      transform,
      maskClass,
      run,
      user
    }
  }
}
</script>

<style scoped>

.triangle {
  position: relative;
  width: 150px;
  height: 150px;
  background-color: white;
  clip-path: polygon(50% 0%, 0% 100%, 100% 100%);
  z-index: 3;
  top: 80vh;
}

.decorate-enter-active {
  animation: fade2 1.2s linear;
}

.decorate-leave-active {
  animation: fade .6s;
}

@keyframes fade2 {
  0% {
    opacity: 0;
  }
  30% {
    opacity: 1%;
  }
  100% {
    opacity: 100%;
  }
}

@keyframes fade {
  from {

  }
  to {
    opacity: 0;
  }
}

.finish-leave-active, .finish-enter-active {
  animation: 1s;
}

.avatar-enter-active {
  animation: avatar2 1s;
}

.avatar-leave-active {
  animation: avatar .7s;
}

@keyframes avatar2 {
  0% {
    transform: scale(0);
    opacity: 0;
    box-shadow: 0 0 0;
    border: 0;
  }
  20% {
    box-shadow: 0 0 0;
    border: 0;
  }
  60% {
    transform: scale(120%);
    opacity: 100%;
  }
  100% {
    transform: scale(100%);
  }
}

@keyframes avatar {
  60% {
    transform: scale(120%);
    opacity: 100%;
  }
  80% {
    box-shadow: 0 0 0;
    border: 0;
  }
  100% {
    transform: scale(0);
    opacity: 0;
  }
}

.close-enter-active {
  animation: close .7s reverse;
}

.close-leave-active {
  animation: close 1s;
}

@keyframes close {
  0% {
    transform: translateY(-36%) rotateX(0);
  }
  50% {
    opacity: 100%;
  }
  100% {
    transform: translateY(-50%) rotateX(90deg);
    opacity: 0;
  }
}

.back-leave-active {
  animation: back 1s;
}

.back-enter-active {
  animation: back2 .6s reverse linear;
}

@keyframes back2 {
  from {

  }
  to {
    transform: translateY(100%);
    opacity: 0;
  }
}

@keyframes back {
  40% {
    transform: translateY(0);
  }
  /*90%{*/
  /*  clip-path: polygon(0 67%, 15% 73%, 50% 100%, 85% 73%, 100% 67%, 100% 100%, 1% 100%);*/
  /*}*/
  50% {
    opacity: 100%;
  }
  100% {
    transform: translateY(100%);
    opacity: 0;
  }
}

.start {
  position: absolute;
  left: 0;
  top: 0;
  width: 100%;
  height: 100vh;
  background-color: transparent;
  z-index: 200;
  overflow: hidden;
}

.surface-avatar {
  position: absolute;
  width: 200px;
  height: 200px;
  border-radius: 100%;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  margin: auto;
  z-index: 3;
  overflow: hidden;
  transition: .5s;
  border: 5px solid transparent;
  background: linear-gradient(45deg, #85FFBD 0%, #FFFB7D 100%);
}

.image-avatar {
  width: 100%;
  height: 200px;
  min-height: 200px;
}

.surface-avatar:hover {
  box-shadow: 0 0 6px #ffed7e, 0 0 12px #ffeda9, 0 0 20px #fffad1, 0 0 32px white, 0 0 45px white;
}

.page-close {
  position: absolute;
  width: 100%;
  height: 80vh;
  transform: translateY(-36%);
  background-color: #62b1ff;
  border-radius: 0 0 100% 100%;
  z-index: 1;
  box-shadow: 0 0 3px gray, 0 0 5px gray, 0 0 10px gray;
}

.shadow-wrap {
  position: absolute;
  width: 100%;
  height: 100vh;
  filter: drop-shadow(0 0 5px gray);
}

.page-back {
  width: 100%;
  height: 100vh;
  background-color: #ffdde5;
  clip-path: polygon(0 12%, 15% 18%, 50% 35%, 85% 18%, 100% 12%, 100% 100%, 0 100%);
}

.wrapper-motto {
  position: absolute;
  width: 100%;
  top: 75vh;
  left: 50vw;
}

@media (max-width: 719px) {
  .surface-avatar {
    width: 150px;
    height: 150px;
  }

  .image-avatar {
    height: 150px;
    min-height: 150px;
  }

  .page-back {
    clip-path: polygon(0 12%, 15% 18%, 50% 30%, 85% 18%, 100% 12%, 100% 100%, 0 100%);
  }
}

@media (min-width: 1100px) {
  .surface-avatar {
    width: 250px;
    height: 250px;
  }

  .image-avatar {
    height: 250px;
    min-height: 250px;
  }
}

</style>
