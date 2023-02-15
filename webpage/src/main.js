import {createApp} from 'vue'
import App from './App.vue'
import router from "@/router";

import VMdPreview from '@kangc/v-md-editor/lib/preview';
import '@kangc/v-md-editor/lib/style/preview.css';
import VMdEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
// VuePress主题以及样式（这里也可以选择github主题）
// import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js';
// import '@kangc/v-md-editor/lib/theme/style/vuepress.css';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';

// 高亮
import hljs from 'highlight.js';

// 选择使用主题
VMdPreview.use(githubTheme, {
    Hljs: hljs
});

VMdEditor.use(githubTheme, {
    Hljs: hljs
});

// 插件合集
import createLineNumbertPlugin from '@kangc/v-md-editor/lib/plugins/line-number/index'
import createCopyCodePlugin from '@kangc/v-md-editor/lib/plugins/copy-code/index';
import '@kangc/v-md-editor/lib/plugins/copy-code/copy-code.css';
import createEmojiPlugin from '@kangc/v-md-editor/lib/plugins/emoji/index';
import '@kangc/v-md-editor/lib/plugins/emoji/emoji.css';

VMdPreview.use(createLineNumbertPlugin());
VMdPreview.use(createCopyCodePlugin());
VMdPreview.use(createEmojiPlugin());

const app = createApp(App)
app.use(router)
app.use(VMdPreview)
app.use(VMdEditor)
app.mount('#app')
