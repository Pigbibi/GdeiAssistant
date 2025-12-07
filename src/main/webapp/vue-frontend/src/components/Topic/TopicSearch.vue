<template>
    <div class="weui-tab">
        <div class="weui-tab__panel">
            <div class="weui-cells__title" @click="goBack">返回</div>
            <div class="hd">
                <h1 class="page_title">搜索话题</h1>
            </div>
            <!-- 搜索表单 -->
            <div class="weui-search-bar" id="searchBar">
                <form class="weui-search-bar__form">
                    <div class="weui-search-bar__box">
                        <i class="weui-icon-search"></i>
                        <input id="searchInput" v-model="keyword" class="weui-search-bar__input" type="search" placeholder="搜索">
                        <a href="javascript:" class="weui-icon-clear" id="searchClear"></a>
                    </div>
                    <label @click="searchTopic" class="weui-search-bar__label" id="searchText">
                        <i class="weui-icon-search"></i>
                        <span>搜索</span>
                    </label>
                </form>
                <a href="javascript:" class="weui-search-bar__cancel-btn" id="searchCancel">取消</a>
            </div>
            <div class="weui-cells" id="searchResult">
                <!-- 话题搜索结果列表 -->
                <div class="weui-cells weui-cells_after-title">
                    <span id="topic_list_span"></span>
                    <!-- 话题列表项 -->
                    <div v-for="(topic, index) in topicList" :key="index" class="weui-cell weui-cell_access" @click="viewTopicDetail(topic.id)">
                        <div class="weui-cell__bd weui-cell_primary">
                            <div class="topic-title">
                                <p class="topic_line">#</p>
                                <p class="topic_name">{{ topic.topic }}</p>
                                <p class="topic_line">#</p>
                            </div>
                            <p class="topic_content">{{ topic.content }}</p>
                            <p class="topic_time">{{ formatDate(topic.publishTime) }}</p>
                            <!-- 话题图片 -->
                            <div v-if="topic.imageCount > 0" class="weui-uploader__files topic-images" id="topic_images">
                                <div v-for="imgIndex in topic.imageCount" :key="imgIndex" class="weui-uploader__file" :style="{backgroundImage: `url(${getTopicImage(topic.id, imgIndex)})`}" @click.stop="showImagePreview(topic.id, imgIndex, index)"></div>
                            </div>
                        </div>
                        <div class="weui-cell__ft weui-cell__ft_in-access">
                            <div class="topic-like-div" @click.stop="likeTopic(topic, index)">
                                <img v-if="topic.like" src="../../../static/img/topic/like.png" class="topic-like-img">
                                <img v-else src="../../../static/img/topic/unlike.png" class="topic-like-img">
                                <span class="topic-like-count">{{ topic.likeCount }}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- 加载更多 -->
            <div class="weui-loadmore" id="topic_loading_div">
                <i class="weui-loading"></i>
                <span class="weui-loadmore__tips">加载中</span>
            </div>
            <div id="no_more_topic_div" style="display: none;">
                <p class="no-more">没有更多话题数据</p>
            </div>
            <!-- 图片预览 -->
            <div class="weui-gallery" id="gallery" v-if="showGallery">
                <span class="weui-gallery__img" id="galleryImg" :style="{backgroundImage: `url(${selectedImage})`}"></span>
                <div class="weui-gallery__opr">
                    <a href="javascript:" class="weui-gallery__del" id="galleryDelete"></a>
                </div>
            </div>
        </div>
        <div class="weui-tabbar">
            <a href="javascript:" @click="$router.push('/topic')" class="weui-tabbar__item">
                <img src="/img/topic/home.png" class="weui-tabbar__icon">
                <p class="weui-tabbar__label">首页</p>
            </a>
            <a href="javascript:" @click="$router.push('/topic/publish')" class="weui-tabbar__item">
                <img src="/img/topic/publish.png" class="weui-tabbar__icon">
                <p class="weui-tabbar__label">发布</p>
            </a>
            <a href="javascript:" @click="$router.push('/topic/search')" class="weui-tabbar__item weui-bar__item_on">
                <img src="/img/topic/search.png" class="weui-tabbar__icon">
                <p class="weui-tabbar__label">搜索</p>
            </a>
        </div>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    name: 'TopicSearch',
    data() {
        return {
            keyword: '',
            topicList: [],
            start: 0,
            size: 10,
            loading: false,
            noMore: false,
            showGallery: false,
            selectedImage: '',
            galleryIndex: 0,
            scrollLock: false
        }
    },
    mounted() {
        // 搜索框自动获取焦点
        const searchInput = document.getElementById('searchInput');
        if (searchInput) {
            searchInput.focus();
        }
        
        // 监听滚动事件
        window.addEventListener('scroll', this.handleScroll);
    },
    beforeDestroy() {
        window.removeEventListener('scroll', this.handleScroll);
    },
    methods: {
        // 返回上一页
        goBack() {
            this.$router.back();
        },
        
        // 搜索话题
        searchTopic() {
            if (!this.keyword.trim()) {
                this.$toptip('请输入搜索关键词', 'error');
                return;
            }
            
            // 重置搜索状态
            this.topicList = [];
            this.start = 0;
            this.noMore = false;
            this.loading = true;
            
            // 调用搜索接口
            axios.get(`/api/topic/keyword/${this.keyword}/start/${this.start}/size/${this.size}`)
                .then(response => {
                    this.loading = false;
                    if (response.data.success) {
                        this.topicList = response.data.data;
                        this.start += this.size;
                        
                        // 检查是否有更多数据
                        if (response.data.data.length < this.size) {
                            this.noMore = true;
                        }
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
        },
        
        // 加载更多话题
        loadMoreTopics() {
            if (this.loading || this.noMore || !this.keyword.trim()) {
                return;
            }
            
            this.loading = true;
            
            axios.get(`/api/topic/keyword/${this.keyword}/start/${this.start}/size/${this.size}`)
                .then(response => {
                    this.loading = false;
                    if (response.data.success) {
                        this.topicList = this.topicList.concat(response.data.data);
                        this.start += this.size;
                        
                        // 检查是否有更多数据
                        if (response.data.data.length < this.size) {
                            this.noMore = true;
                        }
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
        },
        
        // 处理滚动事件
        handleScroll() {
            if (this.scrollLock) {
                return;
            }
            
            const scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
            const scrollHeight = document.documentElement.scrollHeight || document.body.scrollHeight;
            const clientHeight = document.documentElement.clientHeight || window.innerHeight;
            
            // 当滚动到页面底部时，加载更多数据
            if (scrollTop + clientHeight >= scrollHeight - 50) {
                this.scrollLock = true;
                setTimeout(() => {
                    this.loadMoreTopics();
                    this.scrollLock = false;
                }, 100);
            }
        },
        
        // 查看话题详情（简化处理，直接显示提示）
        viewTopicDetail(id) {
            this.$toptip('话题详情功能开发中', 'info');
        },
        
        // 获取话题图片
        getTopicImage(topicId, index) {
            return `/api/topic/id/${topicId}/index/${index}/image`;
        },
        
        // 显示图片预览
        showImagePreview(topicId, imgIndex, topicIndex) {
            this.selectedImage = this.getTopicImage(topicId, imgIndex);
            this.galleryIndex = imgIndex;
            this.showGallery = true;
        },
        
        // 点赞话题
        likeTopic(topic, index) {
            axios.put(`/api/topic/id/${topic.id}/like`)
                .then(response => {
                    if (response.data.success) {
                        // 更新本地话题数据
                        this.$set(this.topicList, index, {
                            ...topic,
                            like: !topic.like,
                            likeCount: topic.like ? topic.likeCount - 1 : topic.likeCount + 1
                        });
                    } else {
                        this.$toptip(response.data.message, 'error');
                    }
                })
                .catch(error => {
                    if (error.response) {
                        this.$toptip(error.response.data.message, 'error');
                    } else {
                        this.$toptip('网络连接失败,请检查网络连接', 'error');
                    }
                });
        },
        
        // 格式化日期
        formatDate(timestamp) {
            const date = new Date(timestamp);
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, '0');
            const day = String(date.getDate()).padStart(2, '0');
            const hours = String(date.getHours()).padStart(2, '0');
            const minutes = String(date.getMinutes()).padStart(2, '0');
            
            return `${year}-${month}-${day} ${hours}:${minutes}`;
        }
    }
}
</script>

<style scoped>
@import '../../../static/css/common/common.css';
@import '../../../static/css/common/weui.min.css';
@import '../../../static/css/topic/index.css';
</style>