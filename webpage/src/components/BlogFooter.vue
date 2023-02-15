<template>
  <div class="wrapper">
    <div class="wrapper-icon">
      <div :class="iconClass"
           @mouseenter="iconAnimate=false;"
           @mouseleave="iconAnimate=true;">{{ name }}
      </div>
      <div class="kusuri-name">
        ルシェット
      </div>
    </div>
    <div class="wrapper-link">
      <a class="icon-github" href="https://github.com/ConfuciusSaid" target="_blank">github</a>
      <a class="icon-tv" href="https://space.bilibili.com/25342195" target="_blank">bilibili</a>
      <a class="icon-terminal" href="https://bbs.kanxue.com/user-home-880853.htm" target="_blank">kanxue</a>
    </div>
  </div>
</template>

<script>
import {onUnmounted, ref} from "vue";

export default {
  name: "BlogFooter",
  setup() {
    let names = ['薬 片', 'くすり'];
    let index = 1;
    let name = ref(names[index]);
    let iconClass = ref("kusuri");
    let iconAnimate = ref(true);

    let switchName = () => {
      iconClass.value = "kusuri action1";
      setTimeout(() => {
        index ^= 1;
        name.value = names[index];
        iconClass.value = "kusuri action2";
      }, 300)
    }

    setTimeout(switchName, 500);

    let timer = setInterval(() => {
      if (iconAnimate.value) {
        switchName()
      }
    }, 5000);
    onUnmounted(() => {
      clearInterval(timer);
    })

    return {
      name,
      iconClass,
      iconAnimate
    }
  }
}
</script>

<style scoped>
.wrapper {
  border-radius: 5px 5px 0 0;
  box-shadow: 0 0 5px gray;
  margin: auto 25px 0;
  display: flex;
}

.wrapper > .wrapper-link, .wrapper-icon {
  display: flex;
  flex-direction: column;
  justify-content: center;
  text-align: center;
}

.wrapper > .wrapper-link > a {
  text-decoration: none;
  color: black;
  margin: 5px;
  transition: .2s;
}

.wrapper > .wrapper-link > a:hover {
  color: #1e53ff;
  text-shadow: 2px 2px 2px dodgerblue;
}

.wrapper > .wrapper-link > a:active {
  color: #471eff;
  text-shadow: 0 0 3px #1e53ff;
}


.wrapper > .wrapper-icon > .kusuri {
  font-weight: bold;
  cursor: default;
  transition: .3s;
  font-family: "KaiTi", serif;
  color: transparent;
  /*text-shadow: 0 0 3px #0ff,*/
  /*0 0 5px #0ff,*/
  /*0 0 10px #0ff,*/
  /*0 0 15px #0ff;*/
  background: #FF3CAC linear-gradient(225deg, #FF3CAC 0%, #784BA0 50%, #2B86C5 100%);
  -webkit-background-clip: text;

  text-shadow: -2px -2px 1px dodgerblue;
}

.wrapper > .wrapper-icon > .kusuri:hover{
  animation: shine 4s infinite alternate;
}

@keyframes shine {
  0%{
    text-shadow: -2px -1px 1px dodgerblue;
  }
  20%{
    text-shadow: 0px 1px 3px #784BA0;
  }
  40%{
    text-shadow: -1px 2px 1px #784BA0;
  }
  60%{
    text-shadow: 1px 0px 3px #FF3CAC;
  }
  80%{
    text-shadow: 2px -2px 1px #2B86C5;
  }
  100%{
    text-shadow: -1px -2px 3px dodgerblue;
  }
}

.wrapper > .wrapper-icon > .kusuri-name {
  font-weight: bold;
  cursor: default;
  margin-top: 12px;
  transition: .3s;
  font-family: "KaiTi", serif;
  color: black;

}

.wrapper > .wrapper-icon > .kusuri-name:hover {
  color: #1e53ff;
  text-shadow: 2px 2px 2px dodgerblue;
}

.wrapper > .wrapper-icon > .kusuri-name:active {
  color: #471eff;
  text-shadow: 0 0 3px #1e53ff;
}

.wrapper > .wrapper-icon > .kusuri.action1 {
  transform: rotateY(90deg);
}

.wrapper > .wrapper-icon > .kusuri.action2 {
  transform: rotateY(0);
}


@media (min-width: 1200px) {
  .wrapper {
    width: 1100px;
    max-width: 1100px;
    margin: auto auto 0 auto;
  }

}

@media (max-width: 719px) {
  .wrapper {
    margin: auto 15px 0;
  }

  .wrapper > .wrapper-icon {
    flex: 2;
  }

  .wrapper > .wrapper-link {
    flex: 1;
  }
}

@media (min-width: 719px) {

  .wrapper > .wrapper-link, .wrapper-icon {
    flex: 1;
  }
}

@media (max-width: 360px) {
  .kusuri {
    font-size: 24px;
  }

  .kusuri-name {
    font-size: 12px;
    letter-spacing: 2px;
  }

  .wrapper > .wrapper-link, .wrapper-icon {
    margin: 5px;
  }
}

@media (min-width: 360px) {
  .kusuri {
    font-size: 40px;
  }

  .kusuri-name {
    font-size: 18px;
    letter-spacing: 5px;
  }

  .wrapper > .wrapper-link, .wrapper-icon {
    margin: 20px;
  }
}
</style>
