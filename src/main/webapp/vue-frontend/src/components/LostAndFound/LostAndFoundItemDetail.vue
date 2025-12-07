<template>
    <div class="detailpage">
        <section class="detail">
            <div class="top">
                <i class="back" @click="goBack">返回</i>
            </div>
            <div class="images">
                <div class="image" v-for="(image, index) in images" :key="index">
                    <img :src="image" @click="showImage(index)">
                </div>
            </div>
            <div class="info">
                <h3>{{ item.name }}</h3>
                <p class="time">发布于 {{ formatTime(item.postTime) }}</p>
                <p class="location">地点：{{ item.location }}</p>
                <p class="description" v-if="item.description">{{ item.description }}</p>
            </div>
            <div class="user">
                <div class="avatar">
                    <img :src="item.userAvatar">
                </div>
                <div class="name">{{ item.userName }}</div>
                <div class="contact">
                    <button @click="showContact">联系TA</button>
                </div>
            </div>
        </section>
        
        <!-- 联系信息弹窗 -->
        <div class="contact-modal" v-if="showContactModal">
            <div class="modal-content">
                <div class="modal-header">
                    <h4>联系方式</h4>
                    <i class="close" @click="hideContact">×</i>
                </div>
                <div class="modal-body">
                    <p>{{ contactInfo }}</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import axios from 'axios'

export default {
    name: 'LostAndFoundItemDetail',
    data() {
        return {
            item: {
                id: '',
                name: '',
                postTime: '',
                location: '',
                description: '',
                userAvatar: '',
                userName: ''
            },
            images: [],
            showContactModal: false,
            contactInfo: ''
        }
    },
    mounted() {
        // 从路由参数获取物品ID
        const itemId = this.$route.params.id;
        
        // 加载物品详情
        this.loadItemDetail(itemId);
        
        // 加载物品图片
        this.loadItemImages(itemId);
        
        // 屏幕适配
        this.screenAdaptation();
    },
    methods: {
        // 返回上一页
        goBack() {
            this.$router.go(-1);
        },
        
        // 格式化时间
        formatTime(time) {
            const date = new Date(time);
            return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')} ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`;
        },
        
        // 屏幕适配
        screenAdaptation() {
            const ua = window.navigator.userAgent;
            if (!(/\(i[^;]+;( U;)? CPU.+Mac OS X/).test(ua) && ua.indexOf('Android') == -1 && ua.indexOf('Linux') == -1) {
                document.body.classList.add('isPC');
            }
            document.body.style.minHeight = window.innerHeight + 'px';
        },
        
        // 加载物品详情
        loadItemDetail(itemId) {
            axios.get(`/api/lostandfound/item/id/${itemId}`)
            .then(response => {
                if (response.data.success === true) {
                    this.item = response.data.data;
                } else {
                    this.$router.push('/lostandfound/lost');
                }
            })
            .catch(error => {
                console.error('加载物品详情失败:', error);
                this.$router.push('/lostandfound/lost');
            });
        },
        
        // 加载物品图片
        loadItemImages(itemId) {
            axios.get(`/api/lostandfound/item/id/${itemId}/images`)
            .then(response => {
                if (response.data.success === true) {
                    this.images = response.data.data;
                }
            })
            .catch(error => {
                console.error('加载物品图片失败:', error);
            });
        },
        
        // 显示图片
        showImage(index) {
            // 这里可以实现图片查看器功能
            console.log('显示图片:', index);
        },
        
        // 显示联系信息
        showContact() {
            // 调用API获取联系信息
            const itemId = this.$route.params.id;
            axios.get(`/api/lostandfound/item/id/${itemId}/contact`)
            .then(response => {
                if (response.data.success === true) {
                    this.contactInfo = response.data.data;
                    this.showContactModal = true;
                } else {
                    alert(response.data.message);
                }
            })
            .catch(error => {
                console.error('获取联系信息失败:', error);
                alert('获取联系信息失败，请稍后重试');
            });
        },
        
        // 隐藏联系信息
        hideContact() {
            this.showContactModal = false;
        }
    }
}
</script>

<style scoped>
    /* 保持原有CSS样式，这里可以添加特定的样式 */
    .detailpage {
        min-height: 100vh;
        background-color: #f5f5f5;
    }
    
    .top {
        height: 50px;
        line-height: 50px;
        text-align: center;
        background-color: #fff;
        border-bottom: 1px solid #eee;
    }
    
    .back {
        float: left;
        margin-left: 20px;
        cursor: pointer;
    }
    
    .images {
        display: flex;
        flex-wrap: wrap;
        padding: 10px;
        background-color: #fff;
    }
    
    .image {
        width: 33.33%;
        padding: 5px;
        box-sizing: border-box;
    }
    
    .image img {
        width: 100%;
        height: auto;
        border-radius: 5px;
    }
    
    .info {
        padding: 15px;
        background-color: #fff;
        margin-top: 10px;
    }
    
    .time {
        color: #999;
        font-size: 14px;
        margin: 5px 0;
    }
    
    .location {
        color: #666;
        margin: 5px 0;
    }
    
    .description {
        color: #333;
        line-height: 1.5;
        margin: 10px 0;
    }
    
    .user {
        display: flex;
        align-items: center;
        padding: 15px;
        background-color: #fff;
        margin-top: 10px;
    }
    
    .avatar {
        width: 50px;
        height: 50px;
        border-radius: 50%;
        overflow: hidden;
        margin-right: 15px;
    }
    
    .avatar img {
        width: 100%;
        height: 100%;
        object-fit: cover;
    }
    
    .name {
        flex: 1;
        font-size: 16px;
    }
    
    .contact button {
        padding: 8px 15px;
        background-color: #007aff;
        color: #fff;
        border: none;
        border-radius: 5px;
        cursor: pointer;
    }
    
    .contact-modal {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0.5);
        display: flex;
        justify-content: center;
        align-items: center;
    }
    
    .modal-content {
        width: 80%;
        max-width: 400px;
        background-color: #fff;
        border-radius: 10px;
        overflow: hidden;
    }
    
    .modal-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 15px;
        border-bottom: 1px solid #eee;
    }
    
    .modal-header h4 {
        margin: 0;
    }
    
    .close {
        font-size: 24px;
        cursor: pointer;
    }
    
    .modal-body {
        padding: 20px;
    }
</style>