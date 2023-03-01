<template>
  <div class="text-wrapper">
    <div class="text-show" ref="textShow" :style="'transform: translateX(-'+transform+'px);'">{{ text }}</div>
    <div :class="((motto==null||motto.length===0)?'mask-shine ':'')+'text-mask '+maskClass" ref="textMask"
         :style="'transform: translateX('+transform+'px);'
                +'background-color:'+color+';'">|
    </div>
  </div>
</template>

<script>
import {onMounted, onUnmounted, ref} from "vue";

export default {
  name: "MottoDisplay",
  props: ['motto', 'color'],
  setup($props) {
    let textList = []

    if (typeof ($props.motto) == "string") {
      textList = $props.motto.split(' ');
    } else if ($props.motto) {
      for (let part of $props.motto) {
        textList.push(part.name)
      }
    }

    let index = 0;
    let text = ref(textList[index]);
    let run = ref(true);

    let transform = ref(0);
    let current = 0;

    let maskClass = ref("");

    let finish, animate;

    let timer;

    finish = function () {
      transform.value -= 4;
      if (transform.value === 0) {
        current = 0;
        index = (index + 1) % textList.length;
        text.value = textList[index];
        timer = setTimeout(animate, 200);
      } else {
        timer = setTimeout(finish, 10);
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
        timer = setTimeout(() => {
          maskClass.value = "";
          finish();
        }, 2200)
      } else if (textList[index][current] === "，") {
        text.value = text.value.replace(/，/g, "");
        timer = setTimeout(animate, 500);
      } else {
        timer = setTimeout(animate, 100);
      }
    }

    onMounted(() => {
      if ($props.motto != null && $props.motto.length !== 0) {
        animate();
      }
    })
    onUnmounted(() => {
      run.value = false;
      clearTimeout(timer);
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
.text-wrapper {
  position: relative;
}

.text-show, .text-mask {
  position: absolute;
  width: 100%;
  /*text-align: center;*/
  white-space: nowrap;
  z-index: 3;
  font-size: 32px;
  font-family: KaiTi, serif;
  font-weight: bold;
  user-select: none;
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
</style>
