<template>
  <div class="wrapper-login">
    <div :class="'login-all la-action'+state">
      <!--1.账号密码登录 2.验证码登录 3.注册-->
      <transition name="fade" mode="out-in">
        <div v-if="state!==2" :class="'account-password ap-action'+state">
          <div class="login-static">账号:</div>
          <input type="text"
                 class="login-text"
                 placeholder="8~12位数字字母"
                 v-model="account"/>
          <div class="login-static">密码:</div>
          <input type="password"
                 class="login-text"
                 placeholder="8~16位数字字母和符号.-_@#~"
                 v-model="password"/>
        </div>
      </transition>
      <transition name="fade" mode="out-in">
        <div v-if="state===3" :class="'password-check pc-action'+state">
          <div class="login-static">
            <span>确认密码:
              <span class="btn-text icon-info"
                    style="text-decoration: none;"
                    @click="$msg.tip('请勿使用自己常用的账号密码' +
                    '<br/>本人不保证数据库一定安全，只有最基本的安全措施'+
                    '<br/>本人有不错的修养，但也请你别假设陌生人道德水平很高',5000);"
              ></span>
            </span>
          </div>
          <input type="password"
                 class="login-text"
                 placeholder="请重复输入密码"
                 v-model="password2"/>
        </div>
      </transition>
      <transition name="fade" mode="out-in">
        <div v-if="state!==1" :class="'code-all ca-action'+state">
          <div class="login-static">
            <span>验证码:
              <span v-show="state===3"
                    class="btn-text icon-question"
                    style="text-decoration: none;"
                    @click="$msg.tip('需要<a style=\'text-decoration: underline;\n'+
                     'color: dodgerblue;\n'+
                     'cursor: pointer;' +
                     'pointer-events: auto\' ' +
                     'href=\'https://live.bilibili.com/24105617\' ' +
                     'target=\'_blank\'>' +
                     '薬片' +
                     '</a>等粉丝牌1级以上' +
                     '<br/>发送验证码前，请公开自己的粉丝牌' +
                     '<br/>可以在公开后重新发送，无需刷新验证码',3500);"
              ></span>
            </span>
            <span class="btn-text" @click="onRefresh();">{{ code ? '刷新' : '获取' }}</span>
          </div>
          <input class="code-text" type="text" :value="code ? ('/auth '+code): ''" disabled="disabled"/>
          <div class="code-tip">
            请发送给指定<a class="btn-text" target="_blank"
                     :href="url">b站号</a>
            &nbsp;<span class="btn-text" @click="copy()">复制</span>
            &nbsp;<span class="btn-text" @click="copyAndSend()">复制并发送</span>
          </div>
        </div>
      </transition>
      <div :class="'btn-changeable bc-action'+state" @click="onClick()">{{ state !== 3 ? '登录' : '注册' }}</div>
    </div>
    <div class="btn-all">
      <transition name="fade" mode="out-in">
        <div v-if="state===1" class="guide-text">
          没有账号？去<span class="btn-text" @click="state=3;">注册</span>
          &nbsp;&nbsp;<span class="btn-text" @click="state=2;">验证码登录</span>
        </div>
        <div v-else-if="state===2" class="guide-text">
          没有账号？去<span class="btn-text" @click="state=3;">注册</span>
          &nbsp;&nbsp;&nbsp;&nbsp;<span class="btn-text" @click="state=1;">密码登录</span>
        </div>
        <div v-else class="guide-text">
          已有账号？去<span class="btn-text" @click="state=1;">登录</span>
          &nbsp;&nbsp;<span class="btn-text" @click="state=2;">验证码登录</span>
        </div>
      </transition>
    </div>
  </div>
</template>

<script>
import {ref} from "vue";
import MsgApi from "@/js/message";
import api from "@/js/api";
import {useRouter} from "vue-router";

export default {
  name: "ForumLogin",
  setup() {
    let account = ref("");
    let password = ref("");
    let password2 = ref("");
    let code = ref(null);
    let state = ref(1);
    let url = "https://message.bilibili.com/?spm_id_from=333.999.0.0#/whisper/mid25342195";
    let router = useRouter();

    function copy() {
      if (code.value == null) {
        MsgApi.error("请先获取验证码");
        return false;
      }
      let input = document.createElement('input');
      input.value = ('/auth ' + code.value);
      input.className = 'copy-tmp-input';
      let login = document.getElementsByClassName('login-all')[0];
      login.appendChild(input);
      input.select();
      document.execCommand('copy');
      input.remove();
      MsgApi.success("复制成功", 1000);
      return true;
    }

    function copyAndSend() {
      if (copy())
        window.open(url);
    }

    function checkAccountAndPassword() {
      return !(!account.value.match(/^[0-9a-zA-Z]{8,12}$/)
          || !password.value.match(/^[0-9a-zA-Z@\-_#~.]{8,16}$/));
    }

    let loginLoading = false;

    function rankCheck(level) {
      if (level <= 8) {
        return "普通的" + level + "级用户";
      } else if (level <= 16) {
        return "亲爱的" + level + "级会员";
      } else if (level <= 24) {
        return "尊敬的" + level + "级贵客";
      } else if (level === 99) {
        return "网站制作者他爹";
      } else {
        return "尊贵的" + level + "级贵宾";
      }
    }

    function doJump() {
      let target = localStorage.getItem("login_target");
      if (target != null) {
        router.push(JSON.parse(target));
        localStorage.removeItem("login_target");
      } else {
        router.push({name: "Forum"});
      }
    }

    function onClick() {
      if (loginLoading) {
        MsgApi.error("点的太快了");
        return;
      }
      switch (state.value) {
        case 1:
          if (!checkAccountAndPassword()) {
            MsgApi.error("账号密码格式错误");
            return;
          }
          loginLoading = true;
          api.loginByPassword(account.value, password.value).then(resp => {
            if (resp.success) {
              MsgApi.success("登录成功，" + rankCheck(resp.data.level));
              localStorage.setItem("kusuri_token", resp.errorMsg)
              doJump();
            }
            loginLoading = false;
          })
          break;
        case 2:
          loginLoading = true;
          api.loginByCode(code.value).then(resp => {
            if (resp.success) {
              MsgApi.success("登录成功，" + rankCheck(resp.data.level));
              localStorage.setItem("kusuri_token", resp.errorMsg)
              doJump();
            }
            loginLoading = false;
          })
          break;
        case 3:
          loginLoading = true;
          if (!checkAccountAndPassword()) {
            MsgApi.error("账号密码格式错误");
            return;
          }
          if (password.value !== password2.value) {
            MsgApi.error("两次输入的密码不一致");
            return;
          }
          if (code.value == null) {
            MsgApi.error("请先发送验证码");
            return;
          }
          api.register(code.value, account.value, password.value).then(resp => {
            if (resp.success) {
              MsgApi.success("注册成功，" + rankCheck(resp.data.level));
              localStorage.setItem("kusuri_token", resp.errorMsg)
              doJump();
            }
            loginLoading = false;
          })
          break;
      }
    }

    let codeLoading = false;

    function onRefresh() {
      if (codeLoading) {
        MsgApi.error("点的太快了");
        return;
      }
      codeLoading = true;
      if (code.value) {
        api.deleteCode(code.value);
      }
      api.getCode().then(resp => {
        code.value = resp.data;
        codeLoading = false;
      })
    }

    return {
      account,
      password,
      password2,
      code,
      state,
      onClick,
      copy,
      copyAndSend,
      url,
      onRefresh
    }
  }
}
</script>

<style scoped>

.copy-tmp-input {
  display: none;
  position: absolute;
}

.fade-enter-active, .fade-leave-active {
  transition: .2s;
}

.fade-enter-from, .fade-leave-to {
  opacity: 0;
}

.wrapper-login {
  width: 100%;
  height: 100vh;
  background: #FA8BFF linear-gradient(45deg, #FA8BFF 0%, #2BD2FF 52%, #2BFF88 90%);

  display: flex;
  flex-direction: column;
  justify-content: center;
}

.login-all, .btn-all {
  align-self: center;
  display: flex;
  flex-direction: column;
  background-color: white;
  padding: 25px;
  border-radius: 10px;
  box-shadow: 0 0 5px gray;
  width: 212px;
  transition: .5s;
  overflow: hidden;
}

.la-action1 {
  height: 192px;
}

.la-action2 {
  height: 127px;
}

.la-action3 {
  height: 366px;
}

.account-password {
  transition: .5s;
  position: absolute;
}

.password-check {
  transition: .5s;
  position: absolute;
}

.code-all {
  transition: .5s;
  position: absolute;
}

.ca-action1 {
  margin-top: 0;
}

.ca-action2 {
  margin-top: 0;
}

.ca-action3 {
  margin-top: 238px;
}

.btn-changeable {
  transition: .5s;
  position: absolute;
}

.bc-action1, .pc-action3 {
  margin-top: 159px;
}

.bc-action2 {
  margin-top: 95px;
}

.bc-action3 {
  margin-top: 333px;
}

.btn-all {
  width: 232px;
  margin-top: 25px;
  flex-direction: row;
  padding-left: 15px;
  padding-right: 15px;
}

.btn-text {
  text-decoration: underline;
  color: dodgerblue;
  cursor: pointer;
}

.btn-text:active {

}

.login-static {
  cursor: default;
  user-select: none;
  display: flex;
  justify-content: space-between;
}

.login-text {
  margin: 5px 0 20px;
  /*height: 30px;*/
  border-radius: 5px;
  padding: 8px 0 8px 10px;
  width: 200px;
  border: 1px solid #d0d7de;
  transition: .5s;
  outline: dodgerblue;

  user-select: none;
}

.login-text:focus-visible, .login-text:focus {
  border: 1px solid dodgerblue;
  box-shadow: 0 0 3px dodgerblue;
}

.btn-changeable {
  width: 211px;
  text-align: center;
  border-radius: 5px;
  padding: 6px 0 6px 0;
  background-color: #40c463;
  color: white;
  font-size: 14px;
  border: 1px green solid;
  cursor: pointer;
  user-select: none;
}

.btn-changeable:hover {
  background-color: #30a14e;
}

.btn-changeable:active {
  background-color: #216e39;
}

.code-text {
  margin: 5px 0 0;
  background-color: #dcdcdc;
  border: 1px solid gray;
  border-radius: 5px;
  font-size: 15px;
  padding: 7px 0 7px 10px;
  color: gray;
  width: 201px;
  cursor: text;
}

.code-tip {
  font-size: 2px;
  margin-bottom: 20px;
  color: gray;
  user-select: none;
}

@media (max-height: 520px) {
  .wrapper-login {
    flex-direction: row;
  }

  .btn-all {
    transition: 0s;
    margin-left: 25px;
    width: 25px;
    writing-mode: tb-rl;
  }
}

.guide-text {
  user-select: none;
}

</style>
