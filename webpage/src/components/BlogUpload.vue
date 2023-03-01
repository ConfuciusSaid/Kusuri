<template>
  <div>
    <transition name="fade" mode="out-in">
      <div v-if="!user.self">
        <error-page text="权限不足" :code="403"/>
      </div>
      <div v-else-if="isEditBlog" class="wrapper-editor">
        <div class="editor-static-wrapper">
          ✨<span class="editor-static">开始创作你的博客吧</span>✨
        </div>
        <div class="editor-item">
          <blog-editor height="211px" :text="titleObj" :edit-only="true" :custom="customBlog"/>
        </div>
        <div class="editor-item">
          <blog-editor height="400px" :text="contentObj" :edit-only="true" :custom="customBlog2"/>
        </div>
        <div class="tags-item-container">
          <tag-editor :length="10" :list="tagsObj" :check="checkTag" name="标签"/>
        </div>
      </div>
      <div v-else class="wrapper-editor">
        <div class="editor-static-wrapper">
          ✨<span class="editor-static">开始编辑你的主页吧</span>✨
        </div>
        <div class="editor-item">
          <blog-editor height="400px" :text="text" :edit-only="true" :custom="customHome"/>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import ErrorPage from "@/components/ErrorPage";
import {inject, nextTick, onMounted, ref, watch} from "vue";
import BlogEditor from "@/components/BlogEditor";
import MsgApi from "@/js/message";
import api from "@/js/api";
import Sortable from "sortablejs";
import TagEditor from "@/components/TagEditor";

export default {
  name: "BlogUpload",
  components: {TagEditor, BlogEditor, ErrorPage},
  setup() {
    let user = inject("user");
    let markdown = ref(localStorage.getItem("home_markdown"));
    if (!markdown.value) markdown.value = "";
    let text = {data: markdown};
    let isEditBlog = ref(true);

    let customHome = [{
      name: "storage",
      icon: "icon-floppy-disk",
      title: "保存草稿",
      action(editor, text) {
        localStorage.setItem("home_markdown", text);
        MsgApi.success("保存成功");
      }
    }, {
      name: "upload",
      icon: 'icon-upload',
      title: '发布为',
      menus: [
        {
          name: "desc",
          text: "个人介绍",
          action(editor, text) {
            if (markdown.value === "") {
              MsgApi.error("您还未输入数据");
            }
            MsgApi.confirm("请确认", "是否发布为个人介绍？", state => {
              if (state === 1) {
                markdown.value = api.defend(markdown.value);
                api.insertDesc(markdown.value).then(resp => {
                  if (resp.success) {
                    MsgApi.success("成功发布个人介绍");
                    markdown.value = "";
                  }
                })
              }
            }, ["是", "否"]);
          }
        },
        {
          name: "info",
          text: "个人信息",
          action(editor, text) {
            if (markdown.value === "") {
              MsgApi.error("您还未输入数据");
            }
            MsgApi.confirm("请确认", "是否发布为个人信息？", state => {
              if (state === 1) {
                markdown.value = api.defend(markdown.value);
                api.insertInfo(markdown.value).then(resp => {
                  if (resp.success) {
                    MsgApi.success("成功发布个人信息");
                    markdown.value = "";
                  }
                })
              }
            }, ["是", "否"]);
          }
        },
        {
          name: "question",
          text: "关于",
          action(editor, text) {
            MsgApi.tip("个人介绍是个人主页的主体部分<br/>" +
                "个人信息则位于主页的侧边栏", 3000);
          }
        }
      ]
    }]

    let title = ref(localStorage.getItem("blog_title"));
    if (!title.value) title.value = "## 博客封面标题\n简单的介绍";
    let titleObj = {data: title};

    let content = ref(localStorage.getItem("blog_content"));
    if (!content.value) content.value = "这里编辑博客的内容";
    let contentObj = {data: content};

    let tags = ref(JSON.parse(localStorage.getItem("blog_tags")));
    if (tags.value === null || tags.length === 0) tags.value = [{name: "默认标签", time: Date.now()}];
    let tagsObj = {data: tags};

    function checkTag(tag) {
      if (tag.length > 32) {
        MsgApi.error("标签长度请勿超过32字符");
        return false;
      }
      return true;
    }

    let customBlog = [{
      name: "storage",
      icon: "icon-floppy-disk",
      title: "保存(所有)",
      action(editor, text) {
        localStorage.setItem("blog_title", title.value);
        localStorage.setItem("blog_content", content.value);
        localStorage.setItem("blog_tags", JSON.stringify(tags.value));
        MsgApi.success("保存成功");
      }
    }, {
      name: "upload",
      icon: 'icon-upload',
      title: '发布博客',
      action(editor, text) {
        let tagStr = "";
        if (tags.value.length === 0) {
          MsgApi.error("请至少添加一个标签");
          return;
        }
        if (title.value === "") {
          MsgApi.error("请编辑博客标题");
          return;
        }
        if (content.value === "") {
          MsgApi.error("请编辑博客内容");
          return;
        }
        for (let tag of tags.value) {
          tagStr += (tag.name + " ");
        }
        title.value = api.defend(title.value);
        content.value = api.defend(content.value);
        api.insertReview(title.value, content.value, tagStr).then(resp => {
          if (resp.success) {
            MsgApi.success(resp.data);
            title.value = "";
            content.value = "";
            tags.value = [{name: "默认标签", time: Date.now()}]
          }
        });
      }
    }, {
      name: "blog",
      icon: 'icon-home',
      title: '去编辑主页',
      action(editor, text) {
        isEditBlog.value = false;
      }
    }, {
      name: "info",
      icon: 'icon-info',
      title: '博客封面',
      action() {
        MsgApi.tip("请在此编辑博客的封面");
      }
    }]
    let customBlog2 = [{
      name: "info",
      icon: 'icon-info',
      title: '博客内容',
      action() {
        MsgApi.tip("请在此编辑博客的内容");
      }
    }]
    customHome.push({
      name: "blog",
      icon: 'icon-books',
      title: '去发布博客',
      action(editor, text) {
        isEditBlog.value = true;
      }
    })

    return {
      user,
      customHome,
      text,
      customBlog,
      customBlog2,
      isEditBlog,
      titleObj,
      contentObj,
      tagsObj,
      checkTag
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

.wrapper-editor {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.editor-item, .tags-item-container {
  border: 1px white solid;
  background-color: #fdfdfd;
  border-radius: 5px;
  box-shadow: 0 0 5px gray;
  margin: 15px 25px 0;
  padding-bottom: 10px;
}

.editor-static-wrapper {
  margin-top: 15px;
  text-align: center;
  filter: drop-shadow(1px 1px 1px gray);
  font-size: 32px;
  font-family: KaiTi, serif;
  user-select: none;
}

.editor-static {
  font-weight: bold;
  cursor: default;
  color: transparent;
  background: #4158D0 linear-gradient(43deg, #4158D0 0%, #C850C0 46%, #FFCC70 100%);

  -webkit-background-clip: text;
  animation: roll 5s infinite alternate;
}

.tags-item-container {
  box-shadow: 0 0 0;
}


@keyframes roll {
  0% {
    background-position: 0;
  }
  30% {
    background-position: 1px;
  }
  70% {
    background-position: 287px;
  }
  100% {
    background-position: 288px;
  }
}

@media (max-width: 719px) {
  .editor-item, .tags-item-container {
    margin-left: 15px;
    margin-right: 15px;
  }
}

@media (min-width: 1200px) {
  .editor-item, .tags-item-container {
    width: 1100px;
    align-self: center;
  }
}
</style>
