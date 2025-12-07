<template>
    <div class="login">
        <!-- 搜索栏 -->
        <section class="search" @click="goToSearch">
            <i class="i"></i>
            <i class="txt">
                <form>
                    <input id="keyword_input" type="text" placeholder="搜搜看" disabled>
                </form>
            </i>
        </section>

        <!-- 常用搜索词汇 -->
        <section class="menus">
            <ul>
                <li class="menu"><a @click="goToType(0)"><i class="i iphone"></i><span
                        class="t">手机</span></a></li>
                <li class="menu"><a @click="goToType(1)"><i class="i icard"></i><span
                        class="t">校园卡</span></a></li>
                <li class="menu"><a @click="goToType(2)"><i class="i iidcard"></i><span class="t">身份证</span></a>
                </li>
                <li class="menu"><a @click="goToType(3)"><i class="i ibank"></i><span
                        class="t">银行卡</span></a></li>
                <li class="menu"><a @click="goToType(4)"><i class="i ibook"></i><span class="t">书</span></a>
                </li>
                <li class="menu"><a @click="goToType(5)"><i class="i ikey"></i><span class="t">钥匙</span></a>
                </li>
                <li class="menu"><a @click="goToType(6)"><i class="i ibag"></i><span class="t">包包</span></a>
                </li>
                <li class="menu"><a @click="goToType(7)"><i class="i icloth"></i><span
                        class="t">衣帽</span></a></li>
                <li class="menu"><a @click="goToType(8)"><i class="i ibicycle"></i><span
                        class="t">校园代步</span></a></li>
                <li class="menu"><a @click="goToType(9)"><i class="i isport"></i><span
                        class="t">运动健身</span></a></li>
                <li class="menu"><a @click="goToType(10)"><i class="i iparts"></i><span
                        class="t">数码配件</span></a></li>
                <li class="menu"><a @click="goToType(11)"><i class="i iother"></i><span
                        class="t">其他</span></a></li>
            </ul>
        </section>

        <!-- 失物招领物品列表 -->
        <section class="lists" :class="{'forp4': isSmallScreen}">
            <div v-for="item in lostItems" :key="item.id" class="list">
                <a @click="goToDetail(item.id)">
                    <i class="img">
                        <img :id="item.id" :src="item.previewImage" @load="checkimg($event.target)">
                    </i>
                    <h5 class="tit">{{ item.name }}</h5>
                    <p class="summary">{{ item.description }}</p>
                </a>
            </div>
        </section>

        <!-- 加载更多 -->
        <section id="loadmore" class="footer" v-if="hasMore" @click="loadMoreInfo">
            <span>点击加载更多</span>
        </section>
        <section v-else class="footer">
            <span>没有更多信息</span>
        </section>

        <div style="height: 4rem;"></div>

        <!-- 底部Tab栏 -->
        <nav class="main-nav">
            <div>
                <a class="on"><i class="ibar index"><img
                        src="/img/lostandfound/lost_selected.png"></i>失物</a>
            </div>
            <div>
                <a @click="goToFound"><i class="ibar ipublish"><img src="/img/lostandfound/found_normal.png"></i>招领</a>
            </div>
            <div class="publish">
                <div class="pubbtn">
                    <a @click="goToPublish">
                        <div class="inner">
                        </div>
                    </a>
                </div>
            </div>
            <div>
                <a @click="goToSearch"><i class="ibar ipublish"><img
                        src="/img/lostandfound/search.png"></i>搜索</a>
            </div>
            <div>
                <a @click="goToPersonal"><i class="ibar iprofile"><img src="/img/lostandfound/personal_normal.png"></i>个人中心</a>
            </div>
        </nav>

        <!-- 错误提示，显示时用$.show();隐藏时用$.hide(); -->
        <div class="weui-toptips weui_warn js_tooltips" v-if="showErrorTip" ref="errorTip">{{ errorMessage }}</div>
    </div>
</template>

<script>
    import axios from 'axios'

export default {
    name: 'LostIndex',
    data() {
        return {
            lostItems: [],
            hasMore: true,
            isSmallScreen: false,
            showErrorTip: false,
            errorMessage: ''
        }
    },
    mounted() {
        // 如果屏幕宽度小于375px，按320px宽的页面缩小
        if (window.innerWidth < 375) {
            this.isSmallScreen = true;
        }
        var ua = window.navigator.userAgent;
        if (!(/\(i[^;]+;( U;)? CPU.+Mac OS X/).test(ua) && ua.indexOf('Android') === -1 && ua.indexOf('Linux') === -1) {
            document.body.classList.add('isPC');
        }
        this.loadInfo(0);
    },
    methods: {
        // 这个图片展示规则是宽大于高按高100%截宽，高大于宽按宽100%截高；默认是宽100%截高
        checkimg(obj) {
            if (obj.width > obj.height) {
                obj.style.width = 'auto';
                obj.style.height = '100%';
            }
        },
        
        //显示错误提示
        showErrorTip(message) {
            this.errorMessage = message;
            this.showErrorTip = true;
            setTimeout(() => {
                this.showErrorTip = false;
            }, 2000);
        },
        
        //加载更多数据
        loadMoreInfo() {
            if (this.hasMore) {
                this.loadInfo(this.lostItems.length);
            }
        },
        
        //加载失物招领信息
        loadInfo(start) {
            this.$toast.loading({
                message: '加载中',
                forbidClick: true,
                duration: 0
            });
            
            axios.get(`/api/lostandfound/lostitem/start/${start}`)
                .then(response => {
                    this.$toast.clear();
                    if (response.data.success) {
                        if (response.data.data && response.data.data.length > 0) {
                            // 为每个物品获取预览图片
                            response.data.data.forEach(item => {
                                item.previewImage = '';
                                this.getPreviewPicture(item);
                                this.lostItems.push(item);
                            });
                        } else {
                            this.hasMore = false;
                        }
                    } else {
                        this.showErrorTip(response.data.message);
                    }
                })
                .catch(error => {
                    this.$toast.clear();
                    this.showErrorTip('网络异常，请检查网络连接');
                    console.error('加载失败:', error);
                });
        },
        
        //获取预览图片
        getPreviewPicture(item) {
            axios.get(`/api/lostandfound/item/id/${item.id}/preview`)
                .then(response => {
                    if (response.data.success) {
                        item.previewImage = response.data.data;
                    }
                })
                .catch(error => {
                    console.error('获取预览图片失败:', error);
                });
        },
        
        // 页面跳转方法
        goToSearch() {
            this.$router.push('/lostandfound/search');
        },
        
        goToType(type) {
            this.$router.push(`/lostandfound/lostinfo/type/${type}`);
        },
        
        goToFound() {
            this.$router.push('/lostandfound/found');
        },
        
        goToPublish() {
            this.$router.push('/lostandfound/publish');
        },
        
        goToPersonal() {
            this.$router.push('/lostandfound/personal');
        },
        
        goToDetail(id) {
            this.$router.push(`/lostandfound/detail/id/${id}`);
        }
    }
}
</script>

<style scoped>
    /* 保持原有CSS样式，这里可以添加特定的样式 */
</style>