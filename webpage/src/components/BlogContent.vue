<template>
  <transition name="fade" mode="out-in">
    <blog-loading v-if="loading"/>
    <div v-else class="wrapper">
      <div v-if="!showContent" class="wrapper">
        <div v-for="blog in blogList" class="desc-item" :key="blog.id" @click="openBlog(blog.id)">
          <v-md-preview :text="blog.title"></v-md-preview>
          <span class="icon-clock2">{{ blog.createTime }}</span>
          <span class="icon-price-tags">{{ blog.tag }}</span>
        </div>
      </div>
      <div v-else class="content-item">
        <div class="open-end-btn" @click="showContent=false;">返回</div>
        <v-md-preview :text="blogContent.markdown"></v-md-preview>
      </div>
      <!--      <v-md-editor v-model="str"/>-->
    </div>
  </transition>
</template>

<script>
import {reactive, ref} from "vue";
import BlogLoading from "@/components/BlogLoading";
import axios from "axios";

export default {
  name: "BlogContent",
  components: {BlogLoading},
  setup() {
    let loading = ref(2);
    let blogList = ref([]);

    axios.get("http://www.kusuri.world/api/blog/blog").then(resp => {
      blogList.value = resp.data;
      loading.value--;
      for (let blogIndex in blogList.value) {
        let blog = blogList.value[blogIndex];
        axios.get("http://www.kusuri.world/api/blog/info/" + blog.id).then(resp => {
          blog.tags = resp.data;
          blog.tag = "";
          for (let tagIndex in resp.data) {
            let tag = resp.data[tagIndex];
            let key = "tag-" + tag.tagId;
            if (!(tag.name = localStorage.getItem(key))) {
              axios.get("http://www.kusuri.world/api/blog/tag/" + tag.tagId).then(resp => {
                localStorage.setItem(key, resp.data);
                tag.name = resp.data;
                blog.tag += tag.name;
              })
            } else {
              blog.tag += (" " + tag.name);
            }
          }
          if (+blogIndex === (blogList.value.length - 1)) {
            loading.value--;
          }
        })
      }
    })

    let showContent = ref(false);
    let blogContent = ref({});

    function openBlog(id) {
      axios.get("http://www.kusuri.world/api/blog/content/" + id).then(resp => {
        blogContent.value = resp.data;
        if (blogContent.value != null) {
          showContent.value = true;
        }
      })
    }

    return {
      loading,
      blogList,
      showContent,
      blogContent,
      openBlog,
      str: ref("")
    }
  }
}
</script>

<style scoped>

.open-end-btn {
  margin: 10px 10px 10px 30px;
  border-radius: 4px;
  box-shadow: 0 0 3px gray;
  text-align: center;
  width: 45px;
  height: 23px;
  cursor: pointer;
  transition: .2s;
}

.open-end-btn:active {
  background-color: dodgerblue;
  color: white;
}

.fade-enter-active, .fade-leave-active {
  transition: .4s;
}

.fade-enter-from, .fade-leave-to {
  opacity: 0;
}

.wrapper {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.desc-item, .content-item {
  border: 1px white solid;
  background-color: #fdfdfd;
  border-radius: 5px;
  box-shadow: 0 0 5px gray;
  margin: 25px 25px 0;
  padding-bottom: 10px;
  flex: 1;
}

.desc-item {
  cursor: pointer;
}

.desc-item > span {
  margin: 10px 10px 10px 4vw;
}

@media (max-width: 719px) {
  .desc-item, .content-item {
    margin-left: 15px;
    margin-right: 15px;
  }
}

@media (min-width: 1200px) {
  .desc-item, .content-item {
    width: 1100px;
    align-self: center;
  }
}
</style>
