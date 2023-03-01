import {createApp} from 'vue'
import App from './App.vue'
import router from "@/router";
import '@/js/message.css';
import MsgApi from '@/js/message.js'


// import ElementPlus from 'element-plus'
// import 'element-plus/dist/index.css'

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
import createTipPlugin from '@kangc/v-md-editor/lib/plugins/tip/index';
import '@kangc/v-md-editor/lib/plugins/tip/tip.css';
import createAlignPlugin from '@kangc/v-md-editor/lib/plugins/align';
// import createKatexPlugin from '@kangc/v-md-editor/lib/plugins/katex/cdn';
// import createMermaidPlugin from '@kangc/v-md-editor/lib/plugins/mermaid/cdn';
// import '@kangc/v-md-editor/lib/plugins/mermaid/mermaid.css';

VMdPreview.use(createLineNumbertPlugin());
VMdPreview.use(createCopyCodePlugin());
VMdPreview.use(createEmojiPlugin());
VMdPreview.use(createTipPlugin());
VMdPreview.use(createAlignPlugin());
// VMdPreview.use(createKatexPlugin());
// VMdPreview.use(createMermaidPlugin());

VMdPreview.xss.extend({
    whiteList: {
        source: [],
        iframe: ['class', 'width', 'height', 'src', 'frameborder', 'allowfullscreen', 'scrolling']
    }
})

VMdEditor.use(createLineNumbertPlugin());
VMdEditor.use(createCopyCodePlugin());
VMdEditor.use(createEmojiPlugin());
VMdEditor.use(createTipPlugin());
VMdEditor.use(createAlignPlugin());
// VMdEditor.use(createKatexPlugin());
// VMdEditor.use(createMermaidPlugin());

VMdEditor.xss.extend({
    whiteList: {
        source: [],
        iframe: ['class', 'width', 'height', 'src', 'frameborder', 'allowfullscreen', 'scrolling']
    }
})

const app = createApp(App)
app.use(router)
app.use(VMdPreview)
app.use(VMdEditor)
// app.use(ElementPlus)
app.mount('#app')
app.config.globalProperties.$msg = MsgApi;
