<template>
  <div class="contain">
    <van-icon class="photograph" name="photograph" />
    <van-field class="login_input" label="用户名" placeholder="请输入用户名" v-model="params.username"/>
    <van-field class="login_input" label="密码" placeholder="请输入密码" v-model="params.password"/>
    <div class="login_btn">
      <van-button @click="submit" size="small" type="primary">登陆</van-button>
    </div>
  </div>
</template>


<script>
import store from "@/store";

export default {
  name: "login",
  data() {
    return {
      params: {
        username:"",
        password:""
      }
    };
  },
  methods: {
    async submit() {
      const response = await this.$post("/user/login",this.params);
      localStorage.setItem("token", response.data);
      store.state.token = response.data;
      const data = localStorage.getItem("token");
      console.log(`response ${data}`);
      this.$router.push({ name: "folder" });
    }
  }
};
</script>


<style  scoped lang="scss">
.login_btn {
  margin-top: 20px;
  text-align: center;
}
.login_input {
  width: 60%;
  margin-top: 20px;
  margin-left: 20%;
  margin-right: 20%;
}
.contain{
  margin-top: 30%;
  text-align: center
}
.photograph{
  margin-top: 20%;
  font-size: 35px;
}
</style>


