import {createRouter, createWebHashHistory, createWebHistory} from 'vue-router'

const ForumLogin = () => import( "@/components/ForumLogin");
const BlogMain = () => import("@/components/BlogMain");
const BlogComment = () => import("@/components/BlogComment");
const BlogHome = () => import("@/components/BlogHome");
const BlogUser = () => import("@/components/BlogUser");
const ForumMain = () => import( "@/components/ForumMain");
const BlogUpload = () => import( "@/components/BlogUpload");
const BlogManage = () => import( "@/components/BlogManage");
const BlogAccept = () => import("@/components/BlogAccept");
const BlogContent = () => import("@/components/BlogContent");
const BlogReview = () => import("@/components/BlogReview");
const ForumUser = () => import("@/components/ForumUser");
const ForumBlog = () => import("@/components/ForumBlog");
const ForumSetting = () => import("@/components/ForumSetting");

const routes = [
    {
        path: '/',
        redirect: {name: 'Forum'}
    },
    {
        path: '/forum',
        name: 'Forum',
        component: ForumMain,
        redirect: {name: "ForumUser"},
        children: [
            {path: '', redirect: {name: 'ForumUser'}},
            {path: 'user/:pageNo(\\d+)?', name: 'ForumUser', component: ForumUser},
            {
                path: 'blog/:pageNo(\\d+)?',
                name: 'ForumBlog',
                component: ForumBlog,
                children: [{
                    path: 'content/:blogId(\\d+)',
                    name: 'ForumBlogContent',
                    component: BlogContent
                }]
            },
            {path: 'setting/:pageNo(\\d+)?', name: 'ForumSetting', component: ForumSetting},
        ]
    },
    {
        path: '/login',
        name: 'Login',
        component: ForumLogin
    },
    {
        path: '/user/:id(\\d+)',
        name: 'User',
        component: BlogMain,
        children: [
            {path: '', name: 'UserSurface'},
            {path: 'home/:pageNo(\\d+)?', name: 'UserHome', component: BlogHome},
            {
                path: 'blog/:pageNo(\\d+)?',
                name: 'UserBlog',
                component: BlogUser,
                children: [{
                    path: 'content/:blogId(\\d+)',
                    name: 'UserBlogContent',
                    component: BlogContent
                }]
            },
            {path: 'comment/:pageNo(\\d+)?', name: 'UserComment', component: BlogComment},
            {path: 'upload/:pageNo(\\d+)?', name: 'BlogUpload', component: BlogUpload},
            {path: 'review/:pageNo(\\d+)?', name: 'BlogReview', component: BlogReview}
        ]
    },
    {
        path: '/manage/',
        name: 'Manage',
        component: BlogManage,
        children: [
            {path: 'accept/:pageNo(\\d+)?', name: 'BlogAccept', component: BlogAccept}
        ]
    }
]

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

export default router
