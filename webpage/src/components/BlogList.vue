<template>
  <div class="wrapper-and-page">
    <div class="wrapper-list">
      <div v-for="blog in blogList" class="desc-item" :key="blog.id">
        <div class="wrapper-editor-btn" v-show="user&&user.self">
          <div v-for="button in buttons"
               :class="button.className+' btn-editor'"
               @click="button.click(blog)">
          </div>
        </div>
        <v-md-preview :text="blog.title" @click="onClick&&onClick(blog)"
                      :style="onClick?'cursor: pointer;':''"></v-md-preview>
        <div>
          <div class="wrapper-title">
            <span class="icon-clock2 title-desc">
              {{
                blog.createTime.substr(0, 16).replace('T', ' ')
              }}
            </span>
            <span class="icon-price-tags title-desc">{{ blog.tag }}</span>
            <span v-if="showName" class="icon-user title-desc">{{ blog.name }}</span>
            <span v-if="showReview"
                  :class="((blog.status==='Reviewing'?'icon-hour-glass':'icon-warning')+' title-desc')">
            {{ blog.status === 'Reviewing' ? '审核中...' : '未通过' }}
          </span>
          </div>
        </div>
      </div>
    </div>
    <page-guide :page="page" :flip="onFlip"></page-guide>
  </div>
</template>

<script>
import PageGuide from "@/components/PageGuide";
import {inject} from "vue";

export default {
  name: "BlogList",
  components: {PageGuide},
  props: ['blogList', 'buttons', 'onClick', 'page', 'onFlip', 'showName', 'showReview'],
  setup($props) {
    let user = inject("user");
    if (user == null) user = {self: false};

    for (let blog of $props.blogList) {
      let tags = blog.tags;
      if (typeof (tags) == 'string') {
        tags = JSON.parse(tags);
      }
      let count = 0;
      blog.tag = "";
      for (let tagName of tags) {
        if (count > 2) {
          blog.tag += " ......";
          break;
        }
        blog.tag += (" " + tagName);
        count++;
      }
      blog.tag = blog.tag.substring(1, blog.tag.length);
    }

    return {
      user
    }
  }
}
</script>

<style scoped>

.wrapper-and-page {
  display: flex;
  flex-direction: column;
}

.wrapper-list {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.desc-item {
  border: 1px white solid;
  background-color: #fdfdfd;
  border-radius: 5px;
  box-shadow: 0 0 5px gray;
  margin: 25px 25px 0;
  padding-bottom: 10px;
  flex: 1;
  display: flex;
  flex-direction: column;
  position: relative;
}

.wrapper-editor-btn {
  margin-left: auto;
  display: flex;
  flex-direction: column;
  justify-content: end;
  position: absolute;
  color: gray;
  bottom: 0;
  right: 0;
}

.btn-editor {
  margin-bottom: 10px;
  margin-right: 10px;
  cursor: pointer;
  transition: .4s;
}

.btn-editor:hover {
  color: dodgerblue;
}

.wrapper-title {
  display: flex;
  padding-left: 25px;
  padding-right: 25px;
  margin-bottom: 5px;
  justify-content: space-around;
}

.title-desc {
  /*margin: 10px 10px 10px 4vw;*/
}

@media (max-width: 719px) {
  .desc-item {
    margin-left: 15px;
    margin-right: 15px;
  }
}

@media (min-width: 1200px) {
  .desc-item {
    width: 1100px;
    align-self: center;
  }
}

</style>
