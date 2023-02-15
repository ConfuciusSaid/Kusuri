<template>
  <transition name="fade" mode="out-in">
    <blog-loading v-if="loading"/>
    <div v-else class="wrapper">
      <div class="wrapper-desc">
        <div v-for="desc in descList" class="desc-item">
          <v-md-preview :text="desc.markdown"></v-md-preview>
        </div>
      </div>
      <div class="wrapper-info">
        <div v-for="info in infoList" class="info-item">
          <v-md-preview :text="info.markdown"></v-md-preview>
        </div>
      </div>
    </div>
  </transition>
</template>

<script>
import {reactive, ref} from "vue";
import BlogLoading from "@/components/BlogLoading";
import axios from "axios"

export default {
  name: "BlogMain",
  components: {BlogLoading},
  setup() {
    let loading = ref(2)
    let descList = ref([])
    let infoList = ref([])
    axios.get("http://www.kusuri.world/api/main/desc").then(resp => {
      descList.value = resp.data;
      loading.value--;
    })
    axios.get("http://www.kusuri.world/api/main/info").then(resp => {
      infoList.value = resp.data;
      loading.value--;
    })

    return {
      loading,
      descList,
      infoList
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

.wrapper {
  display: flex;
  justify-content: center;
}

.wrapper > .wrapper-desc {
  display: flex;
  flex-direction: column;
  flex: 3;
}

.wrapper > .wrapper-info {
  flex-direction: column;
  width: 250px;
}

.desc-item, .info-item {
  border: 1px white solid;
  background-color: #fdfdfd;
  border-radius: 5px;
  box-shadow: 0 0 5px gray;
  margin: 25px 25px 0;
}

.info-item {
  margin-left: 0;
}

@media (max-width: 719px) {
  .wrapper-info {
    display: none;
  }

  .desc-item {
    margin-left: 15px;
    margin-right: 15px;
  }
}

@media (min-width: 719px) {
  .wrapper-info {
    display: flex;
  }
}

@media (min-width: 1200px) {
  .wrapper-desc {
    width: 900px;
    max-width: 900px;
  }

  .wrapper-info {
    width: 900px;
    max-width: 900px;
  }
}
</style>
