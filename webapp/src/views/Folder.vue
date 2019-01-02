<template>
  <div>
    <van-list
      class="list"
      v-model="loading"
      :finished="finished"
      finished-text="没有更多了"
      @load="onLoad"
      :immediate-check="true"
    >
      <van-cell
        v-for="(item,index) in list"
        :key="index"
        :title="item.foldersName"
        is-link
        :to="{name:'gallery',query:{folderId:item.id}}"
      />
    </van-list>
    <van-icon class="icon" @click="addFolder" name="add"/>
    <van-dialog v-model="show" show-cancel-button :before-close="beforeClose">
      <van-field v-model="folderName" label="目录名称" placeholder="请输入"/>
    </van-dialog>
  </div>
</template>

<script>
import { Toast } from "vant";
export default {
  async created() {
    // this.getFolder();
  },
  data() {
    return {
      folderName: "",
      page: {
        pageNum: 1,
        pageSize: 10
      },
      show: false,
      list: [],
      loading: false,
      finished: false
    };
  },
  methods: {
    async addFolder() {
      this.show = true;
      this.folderName = "";
    },
    async getFolder() {
      this.loading = true;
      const response = await this.$fetch("/folder", this.page).catch((error) => {
          Toast.fail(error.message);
        this.loading = false;
        this.finished = true;
      });
      if (response) {
        if (response.data.pageData.lastPage === true) {
          this.finished = true;
        }
        this.list = this.list.concat(response.data.list);
        this.loading = false;
      }
    },
    async beforeClose(action, done) {
      if (action === "confirm") {
        const response = await this.$post("/folder/add", {
          folderName: this.folderName
        });
        if (response.code === 0) {
          done();
          this.list = [];
          this.page = {
            pageNum: 1,
            pageSize: 10
          };
          this.finished = false;
        } else {
          Toast.fail(response.message);
        }
      } else {
        done();
      }
    },
    async onLoad() {
      await this.getFolder();
      this.page.pageNum++;
    }
  }
};
</script>
<style lang="scss" scoped>
.icon {
  position: fixed;
  right: 30px;
  bottom: 20px;
  z-index: 9999;
  font-size: 50px;
}
</style>
