<template>
  <v-md-editor
      :disabled-menus="[]"
      :left-toolbar="'undo redo clear |' +
      'h bold italic strikethrough quote |' +
      'ul ol table hr |' +
      'link image code |' +
      'emoji tip layout |' +
      (editOnly?'':'finish exit ')+customs"
      v-model="text.data"
      :toolbar="toolbar"
      :height="height"
      @upload-image="uploadImage"/>
</template>

<script>
import {ref} from "vue";
import api from "@/js/api";
import MsgApi from "@/js/message";

export default {
  name: "BlogEditor",
  props: ['text', 'finish', 'exit', 'update', 'editOnly', 'custom', 'height'],
  setup($props) {
    let customs = "";
    let markdown = $props.text.data;

    function action(editor, text) {
      editor.insert(function (selected) {
        const prefix = `::: ${text}\n`;
        const suffix = '\n:::';
        const placeholder = '请输入文本';
        const content = selected || placeholder;

        return {
          text: `${prefix}${content}${suffix}`,
          selected: content,
        };
      });
    }

    let toolbar = {
      layout: {
        title: '文字布局',
        icon: 'icon-paragraph-center',
        menus: [
          {name: 'layout1', text: '水平靠左', action: e => action(e, "align-left")},
          {name: 'layout2', text: '水平居中', action: e => action(e, "align-center")},
          {name: 'layout3', text: '水平靠右', action: e => action(e, "align-right")}
        ]
      },
      exit: {
        title: '退出(不保存)',
        icon: 'icon-exit',
        action() {
          $props.exit(markdown.value);
        }
      },
      finish: {
        title: $props.update ? '保存' : '上传',
        icon: $props.update ? 'icon-floppy-disk' : 'icon-upload',
        action() {
          $props.finish(markdown.value);
        }
      }
    };
    if ($props.custom) {
      for (let item of $props.custom) {
        toolbar[item.name] = {
          title: item.title,
          icon: item.icon,
          menus: item.menus,
          action(e, text) {
            item.action(e, markdown.value);
          }
        }
        customs += (item.name + " ");
      }
    }
    toolbar['custom'] = $props.custom ?? null;

    function uploadImage(event, insertImage, files) {
      api.uploadImage(files[0]).then(resp => {
        if (resp.success) {
          MsgApi.success("上传图片成功");
          insertImage({
            url: resp.data,
            desc: files[0].name
          })
        }
      })
    }

    return {
      markdown,
      toolbar,
      customs,
      uploadImage
    }
  }
}
</script>

<style scoped>

</style>
