import http from "@/js/http"
import MsgApi from "@/js/message"

async function doResult(promise) {
    return promise.then(resp => {
        // console.log(resp);
        if (!resp.data.success) {
            MsgApi.error(resp.data.errorMsg);
        }
        return resp.data;
    }).catch(e => {
        MsgApi.error(e.message);
    });
}

export default {
    defend(text) {
        return text.replace(/<[\S\s]+>/g, "");
    },
    getToken() {
        http.getToken()
    },
    async uploadImage(file) {
        let formData = new FormData();
        formData.append("file", file);
        return doResult(http.post("/image/upload", null, formData));
    },
    async getDescByUserId(id, page = 1, limit = 5) {
        return doResult(http.get("/home/desc/" + id, {page, limit}));
    },
    async getInfoByUserId(id) {
        return doResult(http.get("/home/info/" + id));
    },
    async getBlogByUserId(id, page = 1, limit = 5) {
        return doResult(http.get("/blog/" + id, {page, limit}));
    },
    async getAllBlogs(page = 1, limit = 5) {
        return doResult(http.get("/blog/", {page, limit}));
    },
    async getBlogInfoById(id) {
        return doResult(http.get("/blog/info/" + id));
    },
    async getTagNameById(id) {
        return doResult(http.get("/blog/tag/" + id));
    },
    async getBlogContentById(id) {
        return doResult(http.get("/blog/content/" + id));
    },
    async getUserById(id) {
        return doResult(http.get("/user/" + id));
        //return axios.get("http://localhost:9100" + "/user/" + id);
    },
    async getAllUsers(page = 1, limit = 10) {
        return doResult(http.get("/user/", {page, limit}));
        //return axios.get("http://localhost:9100" + "/user/" + id);
    },
    async getCode() {
        return doResult(http.get("/user/code"));
    },
    async deleteCode(code) {
        return doResult(http.delete("/user/code/" + code));
    },
    async checkCode(code) {
        return doResult(http.get("/user/code/" + code));
    },
    async checkToken() {
        return http.get("/user/token");
    },
    async checkAdmin(id) {
        return http.get("/user/admin", {id});
    },
    async loginByPassword(account, password) {
        return doResult(http.post("/user/login", {account, password}));
    },
    async loginByCode(code) {
        return doResult(http.post("/user/login", {code}));
    },
    async register(code, account, password) {
        return doResult(http.post("/user/login", {code, account, password}));
    },
    async updateDesc(id, markdown) {
        return doResult(http.put("/home/desc", null, {id, markdown}));
    },
    async updateInfo(id, markdown) {
        return doResult(http.put("/home/info", null, {id, markdown}));
    },
    async deleteDesc(id) {
        return doResult(http.delete("/home/desc/" + id));
    },
    async deleteInfo(id) {
        return doResult(http.delete("/home/info/" + id));
    },
    async insertDesc(markdown) {
        return doResult(http.post("/home/desc", null, {markdown}));
    },
    async insertInfo(markdown) {
        return doResult(http.post("/home/info", null, {markdown}));
    },
    async getReview() {
        return doResult(http.get("/blog/review"));
    },
    async insertReview(title, content, tags) {
        return doResult(http.post("/blog/review", null, {title, content, tags}));
    },
    async cancelReview(id) {
        return doResult(http.delete("/blog/review", {id}));
    },
    async getAllReview(page = 1, limit = 20) {
        return doResult(http.get("/blog/review/all"), {page, limit});
    },
    async acceptBlog(id) {
        return doResult(http.put("/blog/review/manage", {id}));
    },
    async refuseBlog(id) {
        return doResult(http.delete("/blog/review/manage", {id}));
    },
    async deleteBlog(id) {
        return doResult(http.delete("/blog/" + id));
    },
    async getUserComments(id, page = 1, limit = 20) {
        return doResult(http.get("/comment/user/" + id, {page, limit}));
    },
    async insertUserComment(id, markdown) {
        return doResult(http.post("/comment/user/" + id, null, {markdown}));
    },
    async deleteUserComment(id) {
        return doResult(http.delete("/comment/user/" + id));
    },
    async getBlogComments(id, page = 1, limit = 20) {
        return doResult(http.get("/comment/blog/" + id, {page, limit}));
    },
    async insertBlogComment(id, markdown) {
        return doResult(http.post("/comment/blog/" + id, null, {markdown}));
    },
    async deleteBlogComment(id) {
        return doResult(http.delete("/comment/blog/" + id));
    },
    async setMotto(motto) {
        return doResult(http.post("/user/motto", null, {motto}));
    }
}
