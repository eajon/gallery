<template>
  <div class="contain">
    <!-- <waterfall :line="line" :line-gap="200" :watch="list" @reflowed="reflowed" ref="waterfall">
      <waterfall-slot
        v-for="(item,index) in list"
        :width="200"
        :height="300"
        :order="index"
        :key="item.id"
        move-class="item-move"
      >
        <div class="item" :style="item.style" :index="item.index">
          <img class="img" :src="item.url+item.style">
        </div>
      </waterfall-slot>
    </waterfall>-->
    <Waterfall
      :gutterWidth="10"
      :minCol="4"
      :maxCol="6"
      :gutterHeight="10"
      :resizable="true"
      :percent="[1,1]"
      ref="waterfall"
    >
      <WaterfallItem v-for="(item, index) in list" :key="index" class="item animated fadeIn">
        <img class="img" :src="item.url" @click="onClick(index)">
      </WaterfallItem>
    </Waterfall>
    <van-uploader class="uploader" :after-read="onRead" accept="image/*" multiple>
      <!-- <van-button class="btn" size="small" type="primary">增加目录</van-button> -->
      <van-icon class="icon" name="add"/>
    </van-uploader>
    <van-dialog v-model="show" show-cancel-button :before-close="beforeClose">
      <van-field v-model="remark" label="备注" placeholder="请输入"/>
    </van-dialog>
  </div>
</template>

<script>
// import Waterfall from "vue-waterfall/lib/waterfall";
// import WaterfallSlot from "vue-waterfall/lib/waterfall-slot";
import { Waterfall, WaterfallItem } from "vue2-waterfall";
import { Toast } from "vant";
import { ImagePreview } from "vant";
export default {
  async created() {
    this.folderId = this.$route.query.folderId;
    console.log(this.folderId);
    this.getImages();
    window.onscroll = () => this.scroll();
  },
  data() {
    return {
      folderId: 0,
      list: [],
      finished: false,
      show: false,
      remark: "",
      page: {
        pageNum: 1,
        pageSize: 10
      },
      formData: new FormData()
    };
  },
  methods: {
    scroll() {
      var scrollTop =
        document.documentElement.scrollTop || document.body.scrollTop; //变量windowHeight是可视区的高度
      var windowHeight =
        document.documentElement.clientHeight || document.body.clientHeight; //变量scrollHeight是滚动条的总高度
      var scrollHeight =
        document.documentElement.scrollHeight || document.body.scrollHeight; //滚动条到底部的条件
      if (scrollTop + windowHeight == scrollHeight) {
        this.addItems();
      }
    },
    async onClick(index) {
      let imgs = this.list.map(val => {
        return val.url;
      });
      ImagePreview({
        images: imgs,
        startPosition: index,
        onClose() {
          // do something
        }
      });
    },
    async getImages() {
      const response = await this.$fetch("/image", {
        ...this.page,
        folderId: this.folderId
      }).catch(() => {
        this.finished = true;
      });
      // console.log(response.code);
      if (response) {
        if (response.data.pageData.lastPage === true) {
          this.finished = true;
        }
        this.list = this.list.concat(response.data.list);
      }
    },
    async onRead(res) {
      console.log("onread" + res);
      // this.files =this.files.concat(file)
      this.formData = new FormData();
      for (var i = 0; i < res.length; i++) {
        this.formData.append("files", res[i].file);
        // console.log(file[i].content)
      }
      this.formData.append("folder", "gallery");
      this.formData.append("host", "photo");
      this.formData.append("folderId", this.folderId);

      this.show = true;
    },
    async beforeClose(action, done) {
      if (action === "confirm") {
        this.formData.append("remark", this.remark);
        const response = await this.$upload(
          "/image/upload",
          this.formData
        ).catch(error => {
          Toast.fail(error.message);
          this.remark = "";
          done();
        });
        if (response) {
          if (response.code === 0) {
            this.list = [];
            this.page = {
              pageNum: 1,
              pageSize: 10
            };
            this.getImages();
          } else {
            Toast.fail(response.message);
          }
        }
        this.remark = "";
        done();
      } else {
        done();
      }
    },
    async addItems() {
      if (this.finished === false) {
        this.page.pageNum++;
        await this.getImages();
      }
    }
  },
  components: {
    Waterfall,
    WaterfallItem
  }
};
</script>
<style lang="scss" scoped>
.contain {
  width: 100%;
  overflow: hidden;
}
* {
  padding: 0;
  margin: 0;
}
.icon {
  font-size: 50px;
}
.uploader {
  position: fixed;
  right: 30px;
  bottom: 20px;
  z-index: 9999;
}
.img {
  width: 100%;
  display: block;
}
.animated {
  -webkit-animation-duration: 1s;
  animation-duration: 1s;
  -webkit-animation-fill-mode: both;
  animation-fill-mode: both;
}
.fadeIn {
  -webkit-animation-name: fadeIn;
  animation-name: fadeIn;
}

@-webkit-keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}
</style>
