<template>
  <div class="wrapper-tags-item">
    <div class="tags-item" ref="tagItems">
      <div v-for="(tag,index) in tags" class="can-drag tag-item" :key="tag.time">
        <span>{{ tag.name }}</span>
        <span class="tag-close" @click="tagDelete(index)">×</span>
      </div>
      <div class="tag-add-item tag-item">
        <input type="text" ref="inputTag" class="input-add-tag" v-model="tagNew" @blur="tagInsert()"
               @keydown.enter="inputTag.blur()"/>
        <span class="tag-add-btn">＋</span>
      </div>
    </div>
  </div>
</template>

<script>
import MsgApi from "@/js/message";
import {onMounted, ref} from "vue";
import Sortable from "sortablejs";

export default {
  name: "TagEditor",
  props: ["list", "check", "length", "name"],
  setup($props) {
    let tags = $props.list.data;
    let tagNew = ref("");
    let inputTag = ref(null);
    let tagItems = ref(null);

    let length = $props.length;
    let check = $props.check;
    let name = $props.name;

    function tagInsert() {
      inputTag.value.blur();
      if (tagNew.value === "") {
        return;
      }
      if (tags.value.length >= length) {
        MsgApi.error(`请勿添加超过${length}条${name}`);
        return;
      }
      for (let tag of tags.value) {
        if (tagNew.value === tag.name) {
          MsgApi.error(`${name} “` + tagNew.value + "” 已经存在");
          tagNew.value = "";
          return;
        }
      }
      if (!check(tagNew.value)) {
        return;
      }
      tags.value.push({name: tagNew.value, time: Date.now()});
      tagNew.value = "";
    }

    function tagDelete(id) {
      tags.value.splice(id, 1);
    }

    onMounted(() => {
      Sortable.create(tagItems.value, {
        animation: 150,
        onEnd: evt => {
          const targetRow = tags.value.splice(evt.oldIndex, 1)[0]
          tags.value.splice(evt.newIndex, 0, targetRow)
        },
        draggable: ".can-drag"
      })
    })

    return {
      tags,
      tagNew,
      inputTag,
      tagItems,
      tagInsert,
      tagDelete
    }
  }

}
</script>

<style scoped>
.tags-item {
  display: flex;
  flex-wrap: wrap;
  box-shadow: 0 0 0;
  width: 100%;
}

.tag-item {
  padding: 5px 12px;
  margin: 5px 20px 5px 0;
  border-radius: 15px;
  background: #ffc8c0 linear-gradient(90deg, #ffc8c0 0%, #ffb2c1 55%, #ffccd5 100%);

  box-shadow: 0 0 3px gray;
  display: flex;
  user-select: none;
}

.tag-close {
  align-self: center;
  transition: .3s;
  cursor: pointer;
  color: gray;
}

.tag-close:hover {
  color: dodgerblue;
}

.tag-add-btn {
  position: absolute;
  align-self: center;
  margin-left: 2px;
  pointer-events: none;
}

.tag-add-item {
  padding: 2px 2px;
  vertical-align: center;
  color: gray;
}

.tag-add-item:hover {
  color: dodgerblue;
}

.input-add-tag {
  align-self: center;
  width: 0;
  height: 22px;
  padding: 0 .5rem 0 .5rem;
  border-color: transparent;
  background-color: transparent;
  transition: .2s;
  position: relative;
  cursor: pointer;
}

.input-add-tag:focus {
  cursor: text;
  width: 100px;
  height: 23px;
  border-color: lightskyblue;
  border-radius: 11px;
  background-color: white;
  z-index: 3;
}

.wrapper-tags-item {
  margin-right: -20px;
}

@media (max-width: 719px) {
  .tag-item {
    margin: 5px 15px 5px 0;
  }

  .wrapper-tags-item {
    margin-right: -15px;
  }
}


</style>
