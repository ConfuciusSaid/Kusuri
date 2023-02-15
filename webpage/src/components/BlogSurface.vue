<template>
  <transition name="finish">
    <div class="start" v-show="$route.path==='/'">
      <transition name="avatar">
        <div class="surface-avatar" v-show="$route.path==='/'" @click="$route.path==='/'?$router.push('/main'):true">
          <img class="image-avatar" src="http://www.kusuri.world/photos/avatar.png"/>
        </div>
      </transition>
      <transition name="close">
        <div class="page-close" v-show="$route.path==='/'"></div>
      </transition>
      <transition name="back">
        <div class="shadow-wrap" v-show="$route.path==='/'">
          <div class="page-back"></div>
        </div>
      </transition>
      <transition name="decorate">
        <div v-show="$route.path==='/'">
          <div class="text-show" ref="textShow" :style="'transform: translateX(-'+transform+'px);'">{{ text }}</div>
          <div :class="'text-mask '+maskClass" ref="textMask" :style="'transform: translateX('+transform+'px);'">|</div>
        </div>
      </transition>
    </div>
  </transition>
</template>

<script>
import {onMounted, onUnmounted, ref} from "vue";
import {useRoute, useRouter} from "vue-router";

export default {
  name: "BlogSurface",
  setup() {
    let textList = ["不过是些许风霜罢了", "态度是心的面具",
      "孤注一掷，不飞则死", "我们都是小人物", "都想走出平凡的深渊",
      "一个人的坚持会有多难"]
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
      if (current === text.value.length) {
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
      animate();
    })
    onUnmounted(() => {
      run.value = false;
    })


    return {
      text,
      transform,
      maskClass,
      run
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

.text-show, .text-mask {
  position: absolute;
  width: 100%;
  /*text-align: center;*/
  z-index: 3;
  top: 75vh;
  left: 50vw;
  font-size: 32px;
  font-family: KaiTi, serif;
  font-weight: bold;
}

.text-mask {
  background-color: #ffdde5;
}

.mask-shine {
  animation: mask-shine-animate 1s infinite;
}

@keyframes mask-shine-animate {
  0% {
    opacity: 100%;
  }
  50% {
    opacity: 100%;
  }
  51% {
    opacity: 0;
  }
  100% {
    opacity: 0;
  }
}

.decorate-leave-active {
  animation: fade .6s;
}

@keyframes fade {
  from {

  }
  to {
    opacity: 0;
  }
}

.finish-leave-active {
  animation: 1s;
}

.avatar-leave-active {
  animation: avatar .7s;
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


@media (max-width: 719px) {
  .surface-avatar {
    width: 150px;
    height: 150px;
  }

  .image-avatar{
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

  .image-avatar{
    height: 250px;
    min-height: 250px;
  }
}

</style>
