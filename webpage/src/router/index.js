import {createRouter, createWebHashHistory} from 'vue-router'

const BlogComment = () => import("@/components/BlogComment");
const BlogMain = () => import("@/components/BlogMain");
const BlogContent = () => import("@/components/BlogContent");

const routes = [
    {path: '/main', component: BlogMain},
    {path: '/blog', component: BlogContent},
    {path: '/comment', component: BlogComment}
]

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

export default router
