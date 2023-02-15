<template>
  <div
      style="display: flex;flex-direction: column;align-items: stretch;align-content: stretch;height: 100%;justify-content: center">
    <div class="loading">
      <span :class="icon + ' loading-icon'" :style="'background-position:' + (position - 50) + 'px'"/>
      <transition name="show" appear>
      <span class="loading-text" :style="'background-position:' + (position - 50) + 'px'">
        Loading...
      </span>
      </transition>
    </div>
  </div>
</template>

<script>
import {onUnmounted, ref} from "vue";

export default {
  name: "BlogLoading",
  setup() {
    let timers = [];
    let icons = ['icon-bubble', 'icon-home',
      'icon-images', 'icon-music',
      'icon-books', 'icon-user'];
    let index = 0;
    let icon = ref('');

    icon.value = icons[index] + (index % 2 ? '' : ' active');
    timers[0] = setInterval(() => {
      index = (index + 1) % icons.length;
      icon.value = icons[index] + (index % 2 ? '' : ' active');
    }, 850)

    let position = ref(50);
    timers[1] = setInterval(() => {
      position.value = (position.value + 10) % 1000;
      // if(position.value <= 0 || position.value >= 3000){
      //   delta = -delta;
      // }
    }, 30);

    onUnmounted(() => {
      for (let timer in timers) {
        clearInterval(timer);
      }
    })

    return {
      icon,
      position
    }
  }
}
</script>


<style scoped>
.show-enter-active, .show-leave-active {
  transition: 1s ease-in;
}

.show-enter-from, .show-leave-to {
  opacity: 0;
}

.under {
  position: absolute;
  top: 0;
  left: 0;
  width: 100px;
  height: 100px;
  background: linear-gradient(45deg, #0081ff, #1cbbb4, #0081ff);
  z-index: -1;
}

.loading {
  position: relative;
  align-self: center;
  display: flex;
  flex-direction: column;
}

.loading-icon {
  position: relative;
  top: 0;
  left: 0;
  align-self: center;
  transform: rotateY(-90deg);
  transition: .8s;
  font-size: 100px;
  background: linear-gradient(45deg, #0081ff, #1cbbb4);
  color: transparent;
  -webkit-background-clip: text;
}

.loading-icon.active {
  transform: rotateY(90deg);
}

.loading-text {
  margin: 15px;
  align-self: center;
  letter-spacing: 4px;
  font-size: 28px;
  background: linear-gradient(45deg, #0081ff, #1cbbb4, #0081ff);
  color: rgba(0, 0, 0, 0);
  -webkit-background-clip: text;
  font-weight: bold;
}

.loading-letter {
  transform: translateY(100px);
}

</style>
