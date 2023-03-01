<template>
  <div class="wrapper-error">
    <div class="wrapper1">
      <div class="icon-ghost-shadow">
        <div class="icon-ghost">
          <div class="icon-ghost-eye-left">⭕</div>
          <div class="icon-ghost-eye-right">❌</div>
          <div class="icon-ghost-top"></div>
          <div class="icon-ghost-bottoms-wrapper">
            <div class="icon-ghost-bottoms">
              <div v-for="i in 4" class="icon-ghost-bottom" :style="'transform: translateX(-'+trans+'px)'"></div>
            </div>
          </div>
        </div>
      </div>
      <div class="wrapper2">
        <div class="text-code">
          {{ code }}
        </div>
        <div class="text-msg">
          {{ text }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {onUnmounted, ref} from "vue";

export default {
  name: "ErrorPage",
  props: ['text', 'code'],
  setup() {
    let trans = ref(0);
    let timer = setInterval(() => {
      let newTrans = trans.value + 1;
      if (newTrans > 60) {
        newTrans -= 60;
      }
      trans.value = newTrans;
    }, 20);

    onUnmounted(() => {
      clearInterval(timer)
    })
    return {
      trans
    }
  }
}
</script>

<style scoped>
.wrapper-error {
  display: flex;
  flex-direction: column;
  height: 100vh;
  justify-content: center;
  background: #FA8BFF linear-gradient(45deg, #FA8BFF 0%, #2BD2FF 52%, #2BFF88 90%);
  user-select: none;
}

.wrapper1 {
  display: flex;
  justify-content: center;
}

.wrapper2{
  margin-left: 5%;
  display: flex;
  flex-direction: column;
}

.icon-ghost-shadow {
  align-self: center;
  filter: drop-shadow(0 0 7px gray);
}

.icon-ghost {
  display: flex;
  flex-direction: column;
}

.icon-ghost-top {
  background-color: rgba(255, 255, 255, 1);
  width: 180px;
  height: 200px;
  border-radius: 100% 100% 0 0;
}

.icon-ghost-bottoms-wrapper {
  width: 180px;
  height: 30px;
  overflow: hidden;
}

.icon-ghost-bottoms {
  display: flex;
  width: 240px;
}

.icon-ghost-bottom {
  background-color: rgba(255, 255, 255, 1);
  width: 60px;
  height: 20px;
  margin: 0;
  border-bottom-left-radius: 50% 100%;
  border-bottom-right-radius: 50% 100%;
}

.icon-ghost-eye-left, .icon-ghost-eye-right {
  position: absolute;
  font-size: 45px;
  top: 30%;
}

.icon-ghost-eye-right {
  right: 10%;
}

.icon-ghost-eye-left {
  left: 10%;
}


.text-code {
  font-size: 140px;
  align-self: center;
  font-weight: bold;
  color: white;
  text-shadow: 4px 4px 5px gray;
}

.text-msg{
  margin-bottom: 20%;
  text-align: center;
  font-size: 40px;
  font-weight: bold;
  color: white;
  text-shadow: 3px 3px 4px gray;
}

@media (max-width: 719px) {
  .wrapper1{
    flex-direction: column;
  }
}


</style>
