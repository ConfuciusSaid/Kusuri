import axios from "axios"

//let url = "http://www.kusuri.world/api"
// axios.defaults.baseURL = "http://localhost:8080/api"
axios.defaults.baseURL = "http://www.kusuri.world/api"

function getToken() {
    let cookies = document.cookie.split("; ");
    for (let cookieIndex in cookies) {
        let key = cookies[cookieIndex].split("=")[0];
        if (key === "kusuri_token") {
            return cookies[cookieIndex].split("=")[1];
        }
    }
    return null;
}

export default {
    async get(url, params, headers) {
        if(!params) params = {};
        params["kusuri_token"] = localStorage.getItem("kusuri_token");
        return axios.get(url, {params, headers})
    },
    async post(url, params, data, headers) {
        if(!params) params = {};
        params["kusuri_token"] = localStorage.getItem("kusuri_token");
        return axios.post(url, data, {params, headers})
    },
    async put(url, params, data, headers) {
        if(!params) params = {};
        params["kusuri_token"] = localStorage.getItem("kusuri_token");
        return axios.put(url, data, {params, headers})
    },
    async delete(url, params, headers) {
        if(!params) params = {};
        params["kusuri_token"] = localStorage.getItem("kusuri_token");
        return axios.delete(url, {params, headers})
    },
    getToken
}
