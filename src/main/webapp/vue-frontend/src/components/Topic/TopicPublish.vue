<template>
    <div class="weui-tab">
        <div class="weui-tab__panel">
            <div class="weui-cells__title" @click="goBack">返回</div>
            <div class="hd">
                <h1 class="page_title">发布话题</h1>
            </div>
            <span id="dynamic_topic_span"></span>
            <!-- 提交的话题信息表单 -->
            <div class="weui-cells weui-cells_form">
                <!-- 话题标签 -->
                <div class="weui-cell">
                    <div class="weui-cell__hd">
                        <label class="weui-label">话题</label>
                    </div>
                    <div class="weui-cell__bd weui-cell_primary">
                        <p class="topic_line">#</p>
                        <input id="topic" v-model="topicForm.topic" class="weui-input" type="text" maxlength="15" name="topic" placeholder="请输入你的话题" @input="setTopicInputDynamicWidth">
                        <p class="topic_line">#</p>
                    </div>
                </div>
                <!-- 话题内容 -->
                <div class="weui-cell">
                    <div class="weui-cell__hd">
                        <label class="weui-label">内容</label>
                    </div>
                    <div class="weui-cell__bd">
                        <textarea id="content" v-model="topicForm.content" name="content" class="weui-textarea" placeholder="请输入你的内容" rows="3" @input="inputLengthCheck"></textarea>
                        <div class="weui-textarea-counter"><span>{{contentLength}}</span>/250</div>
                    </div>
                </div>
                <!-- 图片上传 -->
                <div class="weui-gallery" id="gallery" v-if="showGallery">
                    <span class="weui-gallery__img" id="galleryImg" :index="galleryIndex" :style="{backgroundImage: `url(${selectedImage})`}"></span>
                    <div class="weui-gallery__opr">
                        <a href="javascript:" class="weui-gallery__del" @click="deleteImage">
                            <i class="weui-icon-delete weui-icon_gallery-delete"></i>
                        </a>
                    </div>
                </div>
                <div class="weui-cell">
                    <div class="weui-cell__bd">
                        <div class="weui-uploader">
                            <div class="weui-uploader__hd">
                                <p class="weui-uploader__title">图片上传（可选）</p>
                                <div class="weui-uploader__info">{{images.length}}/9</div>
                            </div>
                            <div class="weui-uploader__bd">
                                <ul class="weui-uploader__files" id="uploaderFiles">
                                    <li v-for="(image, index) in images" :key="index" class="weui-uploader__file" :style="{backgroundImage: `url(${image})`}" @click="showImagePreview(image, index)"></li>
                                </ul>
                                <div class="weui-uploader__input-box" v-if="images.length < 9">
                                    <input id="uploaderInput" class="weui-uploader__input" type="file" accept="image/*" @change="handleImageUpload">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- 提交按钮 -->
            <div class="weui-btn_area">
                <a class="weui-btn weui-btn_primary" href="javascript:" @click="postTopicForm">提交</a>
            </div>
        </div>
        <!-- 提交中弹框 -->
        <div role="alert" id="loadingToast" v-if="loading" style="display: block;">
            <div class="weui-mask_transparent"></div>
            <div class="weui-toast">
                <span class="weui-primary-loading weui-icon_toast">
                  <span class="weui-primary-loading__dot"></span>
                </span>
                <p class="weui-toast__content">提交中</p>
            </div>
        </div>
        <div class="weui-tabbar">
            <a href="javascript:" @click="$router.push('/topic')" class="weui-tabbar__item">
                <img src="/img/topic/home.png" class="weui-tabbar__icon">
                <p class="weui-tabbar__label">首页</p>
            </a>
            <a href="javascript:" @click="$router.push('/topic/publish')" class="weui-tabbar__item weui-bar__item_on">
                <img src="/img/topic/publish.png" class="weui-tabbar__icon">
                <p class="weui-tabbar__label">发布</p>
            </a>
            <a href="javascript:" @click="$router.push('/topic/search')" class="weui-tabbar__item">
                <img src="/img/topic/search.png" class="weui-tabbar__icon">
                <p class="weui-tabbar__label">搜索</p>
            </a>
        </div>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    name: 'TopicPublish',
    data() {
        return {
            topicForm: {
                topic: '',
                content: ''
            },
            images: [],
            contentLength: 0,
            loading: false,
            showGallery: false,
            selectedImage: '',
            galleryIndex: 0
        }
    },
    methods: {
        // 返回上一页
        goBack() {
            this.$router.back();
        },
        
        // 动态设置话题表单宽度
        setTopicInputDynamicWidth() {
            // 这个功能在Vue中可以通过CSS更好地实现，这里简化处理
        },
        
        // 检测Input文字长度是否超过限制并进行实时字数反馈
        inputLengthCheck() {
            let w = 0;
            for (let i = 0; i < this.topicForm.content.length; i++) {
                let c = this.topicForm.content.charCodeAt(i);
                if ((c >= 0x0001 && c <= 0x007e) || (0xff60 <= c && c <= 0xff9f)) {
                    w++;
                } else {
                    w += 2;
                }
            }
            this.contentLength = this.topicForm.content.length;
            
            // 如果超过限制，截断内容
            if (w > 250) {
                this.topicForm.content = this.topicForm.content.substr(0, this.topicForm.content.length - 1);
                this.contentLength = this.topicForm.content.length;
            }
        },
        
        // 处理图片上传
        handleImageUpload(e) {
            const files = e.target.files;
            if (files.length === 0) {
                return;
            }
            
            const file = files[0];
            const allowTypes = ['image/jpg', 'image/jpeg', 'image/png', 'image/gif'];
            const maxSize = 1024 * 1024 * 5; // 5MB
            
            // 检查文件类型
            if (allowTypes.indexOf(file.type) === -1) {
                this.$toptip('不合法的图片文件类型', 'error');
                return;
            }
            
            // 检查文件大小
            if (file.size > maxSize) {
                this.$toptip('图片文件不能超过5MB', 'error');
                return;
            }
            
            const reader = new FileReader();
            reader.readAsDataURL(file);
            
            reader.onload = (e) => {
                const img = new Image();
                img.src = e.target.result;
                
                img.onload = () => {
                    const canvas = document.createElement('canvas');
                    const ctx = canvas.getContext('2d');
                    canvas.width = img.width;
                    canvas.height = img.height;
                    ctx.drawImage(img, 0, 0, img.width, img.height);
                    
                    // 压缩图片
                    const base64 = canvas.toDataURL('image/jpeg', 0.8);
                    this.images.push(base64);
                };
            };
            
            // 清空文件输入，以便可以重复选择同一文件
            e.target.value = '';
        },
        
        // 显示图片预览
        showImagePreview(image, index) {
            this.selectedImage = image;
            this.galleryIndex = index;
            this.showGallery = true;
        },
        
        // 删除已上传缓存的图片
        deleteImage() {
            this.images.splice(this.galleryIndex, 1);
            this.showGallery = false;
        },
        
        // 提交话题表单
        postTopicForm() {
            if (this.topicForm.topic.trim() === '' || this.topicForm.topic.length > 15) {
                this.$toptip('话题长度不合法', 'error');
                return;
            }
            
            if (this.topicForm.content.trim() === '' || this.contentLength > 250) {
                this.$toptip('内容长度不合法', 'error');
                return;
            }
            
            this.loading = true;
            
            const formData = new FormData();
            formData.append('topic', this.topicForm.topic);
            formData.append('content', this.topicForm.content);
            formData.append('count', this.images.length);
            
            // 添加图片
            if (this.images.length > 0) {
                this.images.forEach((base64, index) => {
                    // 将base64转换为Blob
                    const byteString = atob(base64.split(',')[1]);
                    const ia = new Uint8Array(byteString.length);
                    for (let i = 0; i < byteString.length; i++) {
                        ia[i] = byteString.charCodeAt(i);
                    }
                    const blob = new Blob([ia], {type: 'image/jpeg'});
                    formData.append('images', blob);
                });
            }
            
            // 提交话题信息
            axios.post('/api/topic', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                },
                processData: false,
                contentType: false
            })
            .then(response => {
                this.loading = false;
                if (response.data.success) {
                    this.$router.push('/topic');
                } else {
                    this.$toptip(response.data.message, 'error');
                }
            })
            .catch(error => {
                this.loading = false;
                if (error.response) {
                    this.$toptip(error.response.data.message, 'error');
                } else {
                    this.$toptip('网络连接失败,请检查网络连接', 'error');
                }
            });
        }
    }
}
</script>

<style scoped>
@import '../../../static/css/common/common.css';
@import '../../../static/css/common/weui.min.css';
@import '../../../static/css/topic/publish.css';
</style>