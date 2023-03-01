<template>
  <div v-for="comment in commentList" class="comment-item">
    <div class="wrapper-desc">
      <div class="wrapper-avatar" @click="$router.push({name:'UserSurface',params:{id:comment.userId}})">
        <img class="image-avatar" :src="comment.user.avatar"/>
      </div>
      <div v-show="self&&(user.self||comment.userId===self.id)"
           class="icon-bin btn-bubble-edit"
           @click="onDelete(comment)"/>
      <div class="desc-text">
        <div class="desc-name">
          {{ comment.user.name }}
          <span :class="'desc-level '+rankCheck(comment.user.level)">
            {{ 'Lv' + comment.user.level }}
          </span>
        </div>
        <div class="desc-time">
          {{
            comment.createTime.substr(5, 11).replace('T', ' ')
          }}
        </div>
      </div>
    </div>
    <div class="wrapper-bubble-desc">
      <div class="wrapper-bubble">
        <v-md-preview :text="comment.markdown"/>
      </div>
    </div>
  </div>
  <page-guide :page="page" :flip="onFlip"/>
</template>

<script>
import {inject, ref} from "vue";
import PageGuide from "@/components/PageGuide";

export default {
  name: "CmtPreview",
  components: {PageGuide},
  props: ["page", "onFlip", "onDelete"],
  setup($props) {
    let commentList = ref(null);
    let user = inject("user");
    let self = inject("self");
    commentList.value = $props.page.records;

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
      commentList,
      rankCheck,
      user,
      self
    }
  }
}
</script>

<style scoped>
.comment-item {
  border-radius: 5px;
  margin: 25px 25px 0;
  padding-bottom: 10px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.wrapper-desc {
  display: flex;
  position: relative;
}

.wrapper-avatar {
  width: 40px;
  height: 40px;
  min-width: 40px;
  border-radius: 100%;
  overflow: hidden;
  margin: 0 5px;
  user-select: none;
  cursor: pointer;
}

.image-avatar {
  width: 40px;
  max-height: 40px;
  min-height: 40px;
}

.wrapper-bubble-desc {
  width: 100%;
  display: flex;
  flex-direction: column;
  margin-top: 5px;
  position: relative;
}

.wrapper-bubble {
  border-radius: 10px 32px 32px 32px;
  border: 1px white solid;
  box-shadow: 0 0 5px gray;
  margin-left: 50px;
}

.desc-time {
  font-size: 13px;
  color: #565656;
}

.desc-level {
  margin-left: 5px;
  color: white;
  user-select: none;
}

.level-1 {
  text-shadow: 0 0 3px lawngreen, 0 0 3px lawngreen, 0 0 3px lawngreen,
  0 0 3px lawngreen, 0 0 3px lawngreen, 0 0 3px lawngreen;
}

.level-2 {
  text-shadow: 0 0 3px lightskyblue, 0 0 3px lightskyblue, 0 0 3px lightskyblue,
  0 0 3px lightskyblue, 0 0 3px lightskyblue, 0 0 3px lightskyblue;
}

.level-3 {
  text-shadow: 0 0 3px mediumpurple, 0 0 3px mediumpurple, 0 0 3px mediumpurple,
  0 0 3px mediumpurple, 0 0 3px mediumpurple, 0 0 3px mediumpurple;
}

.level-4 {
  text-shadow: 0 0 3px gold, 0 0 3px gold, 0 0 3px gold,
  0 0 3px gold, 0 0 3px gold, 0 0 3px gold;
}

.level-5 {
  text-shadow: 0 0 3px red, 0 0 3px red, 0 0 3px red,
  0 0 3px red, 0 0 3px red, 0 0 3px red;
}

.desc-text {
  margin-left: 10px;
}

.btn-bubble-edit {
  position: absolute;
  margin-left: 17px;
  right: 0;
  top: 12px;
  cursor: pointer;
  transition: .4s;
  color: gray;
}

.btn-bubble-edit:hover {
  color: dodgerblue;
}

@media (max-width: 719px) {
  .comment-item {
    margin-left: 15px;
    margin-right: 15px;
  }
}

@media (min-width: 1200px) {
  .comment-item {
    width: 1100px;
    align-self: center;
  }
}
</style>
