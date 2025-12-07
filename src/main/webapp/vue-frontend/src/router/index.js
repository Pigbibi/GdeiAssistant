import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
    mode: 'history',
    routes: [
        {
            path: '/',
            component: resolve => require(['@/components/Index/Index'], resolve),
            children: [
                {
                    path: '',
                    redirect: 'function'
                },
                {
                    path: 'function',
                    name: 'Function',
                    component: resolve => require(['@/components/Function/FunctionIndex'], resolve)
                },
                {
                    path: 'info',
                    name: 'Info',
                    component: resolve => require(['@/components/Info/Info'], resolve)
                },
                {
                    path: 'profile',
                    name: 'Profile',
                    component: resolve => require(['@/components/Profile/ProfileIndex'], resolve)
                }
            ]
        },
        {
            path: '/grade',
            name: 'Grade',
            component: resolve => require(['@/components/Grade/Grade'], resolve)
        },
        {
            path: '/authentication',
            name: 'AuthenticationIndex',
            component: resolve => require(['@/components/Authentication/AuthenticationIndex'], resolve)
        },
        {
            path: '/authentication/update',
            name: 'AuthenticationUpdate',
            component: resolve => require(['@/components/Authentication/AuthenticationUpdate'], resolve)
        },
        {
            path: '/news',
            name: 'NewsIndex',
            component: resolve => require(['@/components/News/NewsIndex'], resolve)
        },
        {
            path: '/news/id/:id',
            name: 'NewsDetail',
            component: resolve => require(['@/components/News/NewsDetail'], resolve)
        },
        {
            path: '/phone',
            name: 'PhoneIndex',
            component: resolve => require(['@/components/Phone/PhoneIndex'], resolve)
        },
        {
            path: '/phone/change',
            name: 'PhoneChange',
            component: resolve => require(['@/components/Phone/PhoneChange'], resolve)
        },
        {
            path: '/photograph',
            name: 'PhotographIndex',
            component: resolve => require(['@/components/Photograph/PhotographIndex'], resolve)
        },
        {
            path: '/photograph/upload',
            name: 'PhotographUpload',
            component: resolve => require(['@/components/Photograph/PhotographUpload'], resolve)
        },
        {
            path: '/profile/avatar',
            name: 'Avatar',
            component: resolve => require(['@/components/Profile/Avatar'], resolve)
        },
        {
            path: '/profile/introduction',
            name: 'Introduction',
            component: resolve => require(['@/components/Profile/Introduction'], resolve)
        },
        {
            path: '/profile/privacy',
            name: 'Privacy',
            component: resolve => require(['@/components/Profile/Privacy'], resolve)
        },
        {
            path: '/profile/public',
            name: 'PublicProfile',
            component: resolve => require(['@/components/Profile/PublicProfile'], resolve)
        },
        {
            path: '/profile/personal',
            name: 'PersonalProfile',
            component: resolve => require(['@/components/Profile/PersonalProfile'], resolve)
        },

        {
            path: '/book',
            name: 'Book',
            component: resolve => require(['@/components/Book/Book'], resolve)
        },
        {
            path: '/collection',
            name: 'Collection',
            component: resolve => require(['@/components/Book/Collection'], resolve)
        },
        {
            path: '/collectiondetail',
            name: 'CollectionDetail',
            component: resolve => require(['@/components/Book/CollectionDetail'], resolve)
        },
        {
            path: '/schedule',
            name: 'Schedule',
            component: resolve => require(['@/components/Schedule/Schedule'], resolve)
        },
        {
            path: '/lostandfound/lost',
            name: 'LostIndex',
            component: resolve => require(['@/components/LostAndFound/LostIndex'], resolve)
        },
        {
            path: '/lostandfound/found',
            name: 'FoundIndex',
            component: resolve => require(['@/components/LostAndFound/FoundIndex'], resolve)
        },
        {
            path: '/lostandfound/item/:id',
            name: 'LostAndFoundItemDetail',
            component: resolve => require(['@/components/LostAndFound/LostAndFoundItemDetail'], resolve)
        },
        {
            path: '/evaluate',
            name: 'Evaluate',
            component: resolve => require(['@/components/Evaluate/Evaluate'], resolve)
        },
        {
            path: '/spare',
            name: 'Spare',
            component: resolve => require(['@/components/Spare/Spare'], resolve)
        },
        {
            path: '/cet',
            name: 'CET',
            component: resolve => require(['@/components/CET/Cet'], resolve)
        }, 
        {
            path: '/cet/save', 
            name: 'CetSave', 
            component: resolve => require(['@/components/CET/CetSave'], resolve)
        },
        {
            path: '/data', 
            name: 'DataIndex', 
            component: resolve => require(['@/components/Data/DataIndex'], resolve)
        },
        {
            path: '/kaoyan',
            name: 'Kaoyan',
            component: resolve => require(['@/components/Kaoyan/Kaoyan'], resolve)
        },
        {
            path: '/card',
            name: 'Card',
            component: resolve => require(['@/components/Card/Card'], resolve)
        },
        {
            path: '/cardinfo',
            name: 'CardInfo',
            component: resolve => require(['@/components/Card/CardInfo'], resolve)
        },
        {
            path: '/cardlost',
            name: 'CardLost',
            component: resolve => require(['@/components/Card/CardLost'], resolve)
        },
        {
            path: '/secret',
            name: 'SecretIndex',
            component: resolve => require(['@/components/Secret/SecretIndex'], resolve)
        },
        {
            path: '/secret/publish',
            name: 'SecretPublish',
            component: resolve => require(['@/components/Secret/SecretPublish'], resolve)
        },
        {
            path: '/secret/detail/:id',
            name: 'SecretDetail',
            component: resolve => require(['@/components/Secret/SecretDetail'], resolve)
        },
        {
            path: '/ershou',
            name: 'ErshouIndex',
            component: resolve => require(['@/components/Ershou/ErshouIndex'], resolve)
        },
        {
            path: '/ershou/detail/:id',
            name: 'ErshouDetail',
            component: resolve => require(['@/components/Ershou/ErshouDetail'], resolve)
        },
        {
            path: '/ershou/publish',
            name: 'ErshouPublish',
            component: resolve => require(['@/components/Ershou/ErshouPublish'], resolve)
        },
        {
            path: '/about',
            name: 'AboutIndex',
            component: resolve => require(['@/components/About/AboutIndex'], resolve)
        },
        {
            path: '/about/account',
            name: 'AboutAccount',
            component: resolve => require(['@/components/About/AboutAccount'], resolve)
        },
        {
            path: '/license',
            name: 'AboutLicense',
            component: resolve => require(['@/components/About/AboutLicense'], resolve)
        },
        {
            path: '/security',
            name: 'AboutSecurity',
            component: resolve => require(['@/components/About/AboutSecurity'], resolve)
        },
        {
            path: '/email',
            name: 'EmailIndex',
            component: resolve => require(['@/components/Email/EmailIndex'], resolve)
        },
        {
            path: '/email/change',
            name: 'EmailChange',
            component: resolve => require(['@/components/Email/EmailChange'], resolve)
        },
        {
            path: '/error/common',
            name: 'CommonError',
            component: resolve => require(['@/components/error/Error'], resolve),
            props: {errorTitle: '通用错误', errorMessage: '发生了一个未知错误，请稍后再试。'}
        },
        {
            path: '/error/server',
            name: 'ServerError',
            component: resolve => require(['@/components/error/Error'], resolve),
            props: {
                errorTitle: '500 Internal Server Error',
                errorMessage: '服务器内部错误，管理员正在紧急修复中！'
            }
        },
        {
            path: '/error/404',
            name: 'NotFoundError',
            component: resolve => require(['@/components/error/Error'], resolve),
            props: {errorTitle: '404 Not Found', errorMessage: '抱歉，你访问的页面无法访问'}
        },
        {
            path: '/error/400',
            name: 'BadRequestError',
            component: resolve => require(['@/components/error/Error'], resolve),
            props: {errorTitle: '400 Bad Request', errorMessage: '请求参数不合法'}
        },
        {
            path: '/error/401',
            name: 'UnauthorizedError',
            component: resolve => require(['@/components/error/Error'], resolve),
            props: {
                errorTitle: '401 Unauthorized Error',
                errorMessage: '当前用户组没有权限访问该资源或执行操作'
            }
        },
        {
            path: '/error/405',
            name: 'MethodNotAllowError',
            component: resolve => require(['@/components/error/Error'], resolve),
            props: {errorTitle: '405 Method Not Allow', errorMessage: '请求方法不支持'}
        },
        {
            path: '/login',
            name: 'Login',
            component: resolve => require(['@/components/Login/Login'], resolve)
        },
        {
            path: '/topic',
            name: 'TopicIndex',
            component: resolve => require(['@/components/Topic/TopicIndex'], resolve)
        },
        {
            path: '/topic/search',
            name: 'TopicSearch',
            component: resolve => require(['@/components/Topic/TopicSearch'], resolve)
        },
        {
            path: '/topic/publish',
            name: 'TopicPublish',
            component: resolve => require(['@/components/Topic/TopicPublish'], resolve)
        },
        {
            path: '/dating',
            name: 'DatingIndex',
            component: resolve => require(['@/components/Dating/DatingIndex'], resolve)
        },
        {
            path: '/dating/pick/id/:id',
            name: 'DatingPick',
            component: resolve => require(['@/components/Dating/DatingPick'], resolve)
        },
        {
            path: '/dating/profile/id/:id',
            name: 'DatingDetail',
            component: resolve => require(['@/components/Dating/DatingDetail'], resolve)
        },
        {
            path: '/dating/message',
            name: 'DatingMessage',
            component: resolve => require(['@/components/Dating/DatingMessage'], resolve)
        },
        {
            path: '/dating/publish',
            name: 'DatingPublish',
            component: resolve => require(['@/components/Dating/DatingPublish'], resolve)
        },
        {
            path: '/ershou/detail/id/:id',
            name: 'ErshouDetail',
            component: resolve => require(['@/components/Ershou/ErshouDetail'], resolve)
        },
        {
            path: '/ershou/search/keyword/:keyword',
            name: 'ErshouSearch',
            component: resolve => require(['@/components/Ershou/ErshouSearch'], resolve)
        },
        {
            path: '/ershou/publish',
            name: 'ErshouPublish',
            component: resolve => require(['@/components/Ershou/ErshouPublish'], resolve)
        },
        {
            path: '/ershou/personal',
            name: 'ErshouPersonal',
            component: resolve => require(['@/components/Ershou/ErshouPersonal'], resolve)
        },
        {
            path: '/kaoyan',
            name: 'KaoYan',
            component: resolve => require(['@/components/KaoYan/KaoYan'], resolve)
        },
        {
            path: '/secret',
            name: 'SecretIndex',
            component: resolve => require(['@/components/Secret/SecretIndex'], resolve)
        },
        {
            path: '/secret/detail/id/:id',
            name: 'SecretDetail',
            component: resolve => require(['@/components/Secret/SecretDetail'], resolve)
        },
        {
            path: '/secret/profile',
            name: 'SecretProfile',
            component: resolve => require(['@/components/Secret/SecretProfile'], resolve)
        },
        {
            path: '/secret/publish',
            name: 'SecretPublish',
            component: resolve => require(['@/components/Secret/SecretPublish'], resolve)
        },
        {
            path: '/wechat/attach',
            name: 'WechatAttach',
            component: resolve => require(['@/components/Wechat/WechatAttach'], resolve)
        },
        {
            path: '/yibanattach',
            name: 'YiBanAttach',
            component: resolve => require(['@/components/YiBan/YiBanAttach'], resolve)
        },
        {
            path: '/yibanauthorize',
            name: 'YiBanAuthorize',
            component: resolve => require(['@/components/YiBan/YiBanAuthorize'], resolve)
        },
        {
            path: '/yibanerror',
            name: 'YiBanError',
            component: resolve => require(['@/components/YiBan/YiBanError'], resolve)
        },
        {
            path: '/wechataccount',
            name: 'WechatAccount',
            component: resolve => require(['@/components/Wechat/WechatAccount'], resolve)
        },
        {
            path: '/close',
            name: 'CloseAccount',
            component: resolve => require(['@/components/Account/CloseAccount'], resolve)
        },
        {
            path: '/reading',
            name: 'Reading',
            component: resolve => require(['@/components/Reading/Reading'], resolve)
        },
        {
            path: '/delivery',
            name: 'DeliveryIndex',
            component: resolve => require(['@/components/Delivery/DeliveryIndex'], resolve)
        },
        {
            path: '/delivery/order',
            name: 'DeliveryOrder',
            component: resolve => require(['@/components/Delivery/DeliveryOrder'], resolve)
        },
        {
            path: '/delivery/accept',
            name: 'DeliveryAccept',
            component: resolve => require(['@/components/Delivery/DeliveryAccept'], resolve)
        },
        {
            path: '/delivery/personal',
            name: 'DeliveryPersonal',
            component: resolve => require(['@/components/Delivery/DeliveryPersonal'], resolve)
        },
        {
            path: '/delivery/order/id/:id',
            name: 'DeliveryOrderDetail',
            component: resolve => require(['@/components/Delivery/DeliveryOrderDetail'], resolve)
        },
        {
            path: '/delivery/trade/id/:id',
            name: 'DeliveryTradeDetail',
            component: resolve => require(['@/components/Delivery/DeliveryTradeDetail'], resolve)
        },
        {
            path: '/evaluate',
            name: 'Evaluate',
            component: resolve => require(['@/components/Evaluate/Evaluate'], resolve)
        },
        {
            path: '/feedback',
            name: 'FeedbackIndex',
            component: resolve => require(['@/components/Feedback/FeedbackIndex'], resolve)
        },
        {
            path: '/feedback/faq',
            name: 'FeedbackFaq',
            component: resolve => require(['@/components/Feedback/FeedbackFaq'], resolve)
        },
        {
            path: '/feedback/function',
            name: 'FeedbackFunction',
            component: resolve => require(['@/components/Feedback/FeedbackFunction'], resolve)
        },
        {
            path: '/feedback/ticket',
            name: 'FeedbackTicket',
            component: resolve => require(['@/components/Feedback/FeedbackTicket'], resolve)
        },
        {
            path: '/announcement/publish',
            name: 'AnnouncementPublish',
            component: resolve => require(['@/components/Announcement/AnnouncementPublish'], resolve)
        },
        {
            path: '/lostandfound/lost',
            name: 'LostIndex',
            component: resolve => require(['@/components/LostAndFound/LostIndex'], resolve)
        },
        {
            path: '/lostandfound/found',
            name: 'FoundIndex',
            component: resolve => require(['@/components/LostAndFound/FoundIndex'], resolve)
        },
        {
            path: '/lostandfound/publish',
            name: 'LostAndFoundPublish',
            component: resolve => require(['@/components/LostAndFound/LostAndFoundPublish'], resolve)
        },
        {
            path: '/lostandfound/edit/id/:id',
            name: 'LostAndFoundEdit',
            component: resolve => require(['@/components/LostAndFound/LostAndFoundEdit'], resolve)
        },
        {
            path: '/lostandfound/search/index',
            name: 'LostAndFoundSearch',
            component: resolve => require(['@/components/LostAndFound/LostAndFoundSearch'], resolve)
        },
        {
            path: '/lostandfound/detail/id/:id',
            name: 'LostAndFoundItemDetail',
            component: resolve => require(['@/components/LostAndFound/LostAndFoundItemDetail'], resolve)
        },
        {
            path: '/lostandfound/type/:type',
            name: 'LostAndFoundTypeResult',
            component: resolve => require(['@/components/LostAndFound/LostAndFoundTypeResult'], resolve)
        },
        {
            path: '/lostandfound/search/keyword/:keyword',
            name: 'LostAndFoundSearchResult',
            component: resolve => require(['@/components/LostAndFound/LostAndFoundSearchResult'], resolve)
        },
        {
            path: '/lostandfound/personal',
            name: 'LostAndFoundPersonal',
            component: resolve => require(['@/components/LostAndFound/LostAndFoundPersonal'], resolve)
        },
        //IPAddress模块
        {
            path: '/ipaddress',
            name: 'IPAddress',
            component: resolve => require(['@/components/IPAddress/IPAddress'], resolve)
        },
        //Kaoyan模块
        {
            path: '/kaoyan',
            name: 'Kaoyan',
            component: resolve => require(['@/components/Kaoyan/Kaoyan'], resolve)
        },
        //Card模块
        {
            path: '/card',
            name: 'Card',
            component: resolve => require(['@/components/Card/Card'], resolve)
        },
        //Grade模块
        {
            path: '/grade',
            name: 'Grade',
            component: resolve => require(['@/components/Grade/Grade'], resolve)
        },
        //Schedule模块
        {
            path: '/schedule',
            name: 'Schedule',
            component: resolve => require(['@/components/Schedule/Schedule'], resolve)
        },
        //Reading模块
        {
            path: '/reading',
            name: 'Reading',
            component: resolve => require(['@/components/Reading/Reading'], resolve)
        },
        //Book模块
        {
            path: '/book',
            name: 'Book',
            component: resolve => require(['@/components/Book/Book'], resolve)
        },
        {
            path: '/ipaddress',
            name: 'IPAddress',
            component: resolve => require(['@/components/IPAddress/IPAddress'], resolve)
        },
        {
            path: '/authentication',
            name: 'AuthenticationIndex',
            component: resolve => require(['@/components/Authentication/AuthenticationIndex'], resolve)
        },
        {
            path: '/agreement',
            name: 'UserAgreement',
            component: resolve => require(['@/components/AgreementAndPolicy/UserAgreement'], resolve)
        },
        {
            path: '/policy/cookie',
            name: 'CookiePolicy',
            component: resolve => require(['@/components/AgreementAndPolicy/CookiePolicy'], resolve)
        },
        {
            path: '/policy/social',
            name: 'SocialPolicy',
            component: resolve => require(['@/components/AgreementAndPolicy/SocialPolicy'], resolve)
        },
        {
            path: '/policy/privacy',
            name: 'PrivacyPolicy',
            component: resolve => require(['@/components/AgreementAndPolicy/PrivacyPolicy'], resolve)
        },
        {
            path: '/policy/deletion',
            name: 'DeletionPolicy',
            component: resolve => require(['@/components/AgreementAndPolicy/DeletionPolicy'], resolve)
        },
        {
            path: '/policy/nonDiscrimination',
            name: 'NonDiscrimination',
            component: resolve => require(['@/components/AgreementAndPolicy/NonDiscrimination'], resolve)
        },
        {
            path: '/policy/intellectualProperty',
            name: 'IntellectualProperty',
            component: resolve => require(['@/components/AgreementAndPolicy/IntellectualProperty'], resolve)
        },
        {
            path: '/announcement/publish',
            name: 'AnnouncementPublish',
            component: resolve => require(['@/components/Announcement/AnnouncementPublish'], resolve)
        },
        {
            path: '/announcement/suicidePrevention',
            name: 'SuicidePrevention',
            component: resolve => require(['@/components/Announcement/SuicidePrevention'], resolve)
        },
        {
            path: '/topic',
            name: 'TopicIndex',
            component: resolve => require(['@/components/Topic/TopicIndex'], resolve)
        },
        {
            path: '/topic/publish',
            name: 'TopicPublish',
            component: resolve => require(['@/components/Topic/TopicPublish'], resolve)
        },
        {
            path: '/topic/search',
            name: 'TopicSearch',
            component: resolve => require(['@/components/Topic/TopicSearch'], resolve)
        },
        {
            path: '/wechat/account',
            name: 'WechatAccount',
            component: resolve => require(['@/components/Wechat/WechatAccount'], resolve)
        },
        {
            path: '/wechat/attach',
            name: 'WechatAttach',
            component: resolve => require(['@/components/Wechat/WechatAttach'], resolve)
        }
    ]
})