<template>
    <div class="map-container">

        <div class="chart_box">
            <div class="chart" id="chart_middel2"></div>
        </div>

        <div class="mapPanel">
            <table>
                <tr>
                    <td colspan="2" class="bank_name">
                        工行<span>{{bankName}}</span>分行
                    </td>
                </tr>
                <tr>
                    <td class="title">资本充足率</td>
                    <td class="title">核心资本充足率</td>
                </tr>
                <tr>
                    <td class="index" id="index1">{{index1}}%</td>
                    <td class="index" id="index2">{{index2}}%</td>
                </tr>
                <tr>
                    <td class="long title">核心负债</td>
                    <td class="long title">核心一级</td>
                </tr>
                <tr>
                    <td class="long title">依存度</td>
                    <td class="long title">资本充足率</td>
                </tr>
                <tr>
                    <td class="index" id="index3">{{index3}}%</td>
                    <td class="index" id="index4">{{index4}}%</td>
                </tr>
            </table>
        </div>

    </div>
</template>

<script>
    import echarts from "echarts";
    import {mapGetters, mapState} from "vuex";
    export default {
        name: "mapChart",
        data(){
            return{
                //[{"hxzbczl":11,"ldxbl":35,"province":"安徽分行","zbczl":13,"zblrl":13}
                //地图
                mapName : 'china',
                //地图所需数据
                mapdata : [
                    //name:省份 value:出现风险的指标数
                ],
                //省份及对应的4个指标,从后端传来
                indexData:[
                    // {name:"湖北",index1:10,index2:11,index3:12,index4:13},
                    // {name:"湖南",index1:15,index2:16,index3:17,index4:18},
                    // {name:"重庆",index1:20,index2:21,index3:22,index4:23},
                    // {name:"四川",index1:25,index2:26,index3:27,index4:28},
                    // {name:"贵州",index1:30,index2:31,index3:32,index4:33},
                    // {name:"云南",index1:35,index2:36,index3:37,index4:38},
                    // {name:"西藏",index1:40,index2:41,index3:42,index4:43},
                    // {name:"陕西",index1:45,index2:46,index3:47,index4:48},
                    // {name:"甘肃",index1:50,index2:51,index3:52,index4:53},
                    // {name:"青海",index1:55,index2:56,index3:57,index4:58},
                    // {name:"宁夏",index1:60,index2:61,index3:62,index4:63},
                    // {name:"新疆",index1:65,index2:66,index3:67,index4:68},
                    // {name:"广东",index1:70,index2:71,index3:72,index4:73},
                    // {name:"广西",index1:75,index2:76,index3:77,index4:78},
                ],
                //地图坐标
                geoCoordMap : {
                    '台湾': [121.5135,25.0308],
                    '黑龙江': [127.9688, 45.368],
                    '内蒙古': [110.3467, 41.4899],
                    "吉林": [125.8154, 44.2584],
                    '北京': [116.4551, 40.2539],
                    "辽宁": [123.1238, 42.1216],
                    "河北": [114.4995, 38.1006],
                    "天津": [117.4219, 39.4189],
                    "山西": [112.3352, 37.9413],
                    "陕西": [109.1162, 34.2004],
                    "甘肃": [103.5901, 36.3043],
                    "宁夏": [106.3586, 38.1775],
                    "青海": [101.4038, 36.8207],
                    "新疆": [87.9236, 43.5883],
                    "西藏": [91.11, 29.97],
                    "四川": [103.9526, 30.7617],
                    "重庆": [108.384366, 30.439702],
                    "山东": [117.1582, 36.8701],
                    "河南": [113.4668, 34.6234],
                    "江苏": [118.8062, 31.9208],
                    "安徽": [117.29, 32.0581],
                    "湖北": [114.3896, 30.6628],
                    "浙江": [119.5313, 29.8773],
                    "福建": [119.4543, 25.9222],
                    "江西": [116.0046, 28.6633],
                    "湖南": [113.0823, 28.2568],
                    "贵州": [106.6992, 26.7682],
                    "云南": [102.9199, 25.4663],
                    "广东": [113.12244, 23.009505],
                    "广西": [108.479, 23.1152],
                    "海南": [110.3893, 19.8516],
                    '上海': [121.4648, 31.2891],

                },
                //详情框
                index1:0,
                index2:0,
                index3:0,
                index4:0,
                bankName:' ',
                //定时器
                timer:null,
                limit:[],
                riskCity:[]
            }
        },
        computed: {
            ...mapGetters({
                getValues:'map/getValues',getRisk:'risk/getValues'
                // ...
            }),
            ...mapState([
                'map/values','risk/values'
            ])
        },
        watch:{//监听store的value变化
            getValues:{
                handler(newVal,oldVal) {// eslint-disable-line no-unused-vars
                    this.setLimit();
                    console.log("watch: map store更改！！")
                    clearInterval(this.timer);
                    this.setIndexData()
                    this.getChart(this.mapdata);
                    this.getRiskCity();
                }
            }
        },
        mounted() {
            this.initData();
            // this.setIndexData();
            // this.getChart(this.mapdata);
        },
        methods: {
            async initData(){
                let data1=await this.$H.get('/GetData/MMap');
                this.$store.commit('map/setValues',data1)
            },
            //初始化limit数组
            setLimit(){
              this.limit=[];
              let name=["资本充足率","核心资本充足率","核心负债依存度","核心一级资本充足率"];
              for(let j in this.getValues){
                for(let i in this.getRisk){
                  if(this.getRisk[i].name.toString().localeCompare(name[j])==0){
                    // console.log("相等push: "+this.getRisk[i].value);
                    this.limit.push(this.getRisk[i].value);
                    break;
                  }
                  else{
                    // console.log("不等");
                  }
                }
              }
            },
            setIndexData(){
                this.indexData.length=0;
                for (let i in this.getValues){
                    this.indexData.push({
                        name:this.getValues[i].province,
                        index1:this.getValues[i].zbczl,
                        index2:this.getValues[i].hxzbczl,
                        index3:this.getValues[i].hxfzycd,
                        index4:this.getValues[i].hxyjzbczl
                    });
                }
            },
            //风险城市名单riskCity
            getRiskCity(){
              this.riskCity.length=0;
              for (let i in this.getValues){
                if(this.indexData[i].index1<this.limit[0]||this.indexData[i].index2<this.limit[1]||this.indexData[i].index3<this.limit[2]||this.indexData[i].index4<this.limit[3])
                  this.riskCity.push(this.indexData[i].name);
              }
              // console.log("风险城市名单"+this.riskCity)
            },
            //地图所需要的mapdata
            convertData(mapdata) {
                var res = [];
                for (let i in mapdata) {
                    var geoCoord = this.geoCoordMap[mapdata[i].name];
                    if (geoCoord) {
                        res.push({
                            name: mapdata[i].name,
                            value: geoCoord.concat(mapdata[i].value),
                        });
                    }
                }
                return res;
            },
            //高亮轮播所需要的defaultP
            getDefaultP(mapdata){
                var defaultP=[];
                for (let i in mapdata){
                    if(i==0){
                        defaultP.push(
                            {name:mapdata[i].name,selected: true}
                        );
                    }
                    else {
                        defaultP.push(
                            {name:mapdata[i].name,selected: false}
                        );
                    }
                }
                return defaultP;
            },
            // 根据indexData初始化mapdata
            getMapdata(indexData){
                var mapdata=[];
                for(let i in indexData){
                    mapdata.push({
                        name:indexData[i].name,
                        value:0
                    })
                }
                return mapdata;
            },

            getChart(mapdata){
                mapdata=this.getMapdata(this.indexData);//初始化地图所需数据

                var myChart = echarts.init(document.getElementById('chart_middel2'));
                var defaultP =this.getDefaultP(mapdata);//用于得到默认省份高亮的配置，defaultP设置了当前高亮的省份

                var option = {
                    tooltip:{//小浮框
                        trigger: "item",
                        textStyle: {
                            color: "#fff",
                        },
                        formatter: function(params){
                            var toolTiphtml = '';
                            for(var i = 0;i<mapdata.length;i++){
                                if(params.name==mapdata[i].name){
                                    toolTiphtml+='<span>'+params.name+'</span>'+'省分行<br>'+
                                        '<span style="color:'+'rgba(23, 240, 204)'+'">风险指标数 ：'+mapdata[i].value+'</span>';
                                    break;
                                }
                            }
                            return toolTiphtml;
                        },
                    },
                    geo: {//地图
                        map: this.mapName,
                        show: true,
                        roam: true,//可拖拽、放大
                        // left:11,
                        top:50,
                        label: {
                            emphasis: {
                                show: false,
                                color: "#fff"
                            }
                        },
                        //   放大我们的地图
                        zoom: 1,
                        itemStyle: {
                            normal: {
                                areaColor: "rgba(43, 196, 243, 0.42)",
                                borderColor: "rgba(43, 196, 243, 1)",
                                borderWidth: 0.7
                            },
                            emphasis: {
                                areaColor: "rgba(229, 46, 92, 0.6)"//"rgba(35, 194, 252, 0.8)"//"rgba(229, 46, 92, 0.8)"
                            }//
                        },
                    },
                    // visualMap: {
                    //     min: 0,
                    //     max: 4.5,
                    //     left: 26,
                    //     bottom: 40,
                    //     showLabel: true,
                    //     text: ["高", "低"],
                    //     pieces: [{
                    //         gt: 3.5,
                    //         label: "> 100 人",
                    //         color: "#7f1100"
                    //     }, {
                    //         gte: 3,
                    //         lte: 3.5,
                    //         label: "10 - 100 人",
                    //         color: "#ff5428"
                    //     }, {
                    //         gte: 2,
                    //         lt: 2.5,
                    //         label: "1 - 9 人",
                    //         color: "#ff8c71"
                    //     }, {
                    //         gte: 1,
                    //         lt: 1.5,
                    //         label: "疑似",
                    //         color: "#ffd768"
                    //     }, {
                    //         value: 0,
                    //         color: "#ffffff"
                    //     }],
                    //     show: false,
                    //     zlevel:0,
                    // },
                    series: [
                        {
                            name: '散点值',
                            type: 'scatter',
                            coordinateSystem: 'geo',
                            data: this.convertData(mapdata),
                            symbolSize: 5,
                            label:
                                {
                                    normal: {
                                        formatter: '{b}',
                                        position: 'right',
                                        show: true
                                    },
                                    emphasis: {
                                        show: true
                                    }
                                },
                            itemStyle: {
                                normal: {
                                    color:params=>{
                                      if(this.riskCity.includes(params.data.name,0)){
                                        return "red"
                                      }
                                      else{return "#fff"}
                                    },
                                    // color: '#fff'
                                }
                            }
                        },
                        {
                            name:'省份',
                            type: 'map',
                            map: this.mapName,
                            geoIndex: 0,
                            aspectScale: 0.75, //长宽比
                            showLegendSymbol: false, // 存在legend时显示
                            selectedMode : 'single',
                            label: {
                                normal: {
                                    show: false
                                },
                                emphasis: {
                                    show: true,
                                    textStyle: {
                                        color: '#fff'
                                    }
                                }
                            },
                            roam: true,
                            itemStyle: {
                                normal: {
                                    areaColor: '#031525',
                                    borderColor: '#3B5077',
                                },
                                emphasis: {
                                    areaColor: '#2B91B7',
                                    shadowBlur: 200,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            },
                            animation: false,
                            data: this.convertData(mapdata)
                        },
                        {
                            name: '黄点',
                            type: 'effectScatter',
                            coordinateSystem: 'geo',
                            data: this.convertData(mapdata),
                            symbolSize: function(val) {
                                return (val[2]+2)*3.5;
                            },
                            showEffectOn: 'render',
                            rippleEffect: {
                                brushType: 'stroke'
                            },
                            hoverAnimation: true,
                            label: {
                                normal: {
                                    formatter: '{b}',
                                    position: 'left',
                                    show: false
                                }
                            },
                            itemStyle: {
                                normal: {
                                  color:params=>{
                                    if(this.riskCity.includes(params.data.name,0)){
                                      return "red"
                                    }
                                    else{return "yellow"}
                                  },
                                  // color: 'yellow',
                                  shadowBlur: 10,
                                  shadowColor: 'yellow'
                                }
                            },
                            zlevel: 1
                        },
                        {
                            name: '红色指标',
                            type: 'scatter',
                            coordinateSystem: 'geo',
                            symbol: 'pin',
                            symbolSize: [100,100],
                            label: {
                                normal: {
                                    show: true,
                                    textStyle: {
                                        color: '#fff',
                                        fontSize: 12,
                                        fontWeight: "bold",
                                    },
                                    formatter (value){
                                        return value.data.name+'分行'
                                    }
                                }
                            },
                            itemStyle: {
                                normal: {
                                    color: '#F62157', //标志颜色
                                }
                            },
                            showEffectOn: 'render',
                            rippleEffect: {
                                brushType: 'stroke'
                            },
                            hoverAnimation: true,
                            zlevel: 6,
                            data: []//this.convertData([data[0]]),
                        },
                    ]
                };
                myChart.setOption(option,true);


                //预设空白段
                var currentIndex =  0;//option.series[1].data.length-1;
                console.log("空白段 当前："+currentIndex);
                this.index1=this.indexData[currentIndex].index1;
                this.index2=this.indexData[currentIndex].index2;
                this.index3=this.indexData[currentIndex].index3;
                this.index4=this.indexData[currentIndex].index4;
                this.bankName=this.indexData[currentIndex].name;
                defaultP[currentIndex].selected=true;
                // defaultP[0].selected=false;
                myChart.setOption({
                    geo: {
                        regions: defaultP,//设置省份高亮
                    },
                    series:[
                        {},{},{},
                        {
                            data:this.convertData([mapdata[currentIndex]])
                        },
                    ],
                });
                //定时器
                this.timer = setInterval(()=>{   // eslint-disable-line no-unused-vars
                    var dataLen = option.series[1].data.length;

                    var before=currentIndex;
                    if(before<0){
                        before=dataLen-1;
                    }

                    function f() {//变化要高亮的省份
                        defaultP[before].selected=false;
                        defaultP[currentIndex].selected=true;
                        return defaultP;
                    }

                    // 取消之前高亮的图形
                    // myChart.dispatchAction({
                    //     type: 'downplay',
                    //     seriesIndex: 0
                    // });
                    currentIndex = (currentIndex + 1) % dataLen;
                    console.log("当前："+currentIndex);
                    // 显示 tooltip
                    // myChart.dispatchAction({
                    //     type: 'showTip',
                    //     seriesIndex: 0,
                    //     dataIndex: currentIndex
                    // });

                    // myChart.dispatchAction({
                    //     type: 'geoSelect',          //选中指定的地图区域。
                    //     seriesIndex: 0,  // 可选，系列 index，可以是一个数组指定多个系列
                    //     dataIndex: currentIndex,          // 数据的 index，如果不指定也可以通过 name 属性根据名称指定数据
                    // });

                    //更改红色圈位置
                    myChart.setOption({
                        geo: {
                            regions: f(),//设置省份高亮
                        },
                        series:[
                            {},{},{},
                            {
                                data:this.convertData([mapdata[currentIndex]])
                            },
                        ],
                    });
                    this.index1=this.indexData[currentIndex].index1;
                    this.index2=this.indexData[currentIndex].index2;
                    this.index3=this.indexData[currentIndex].index3;
                    this.index4=this.indexData[currentIndex].index4;
                    this.bankName=this.indexData[currentIndex].name;

                    //小浮框中数值预警
                    let indexData=["index1","index2","index3","index4",]
                    let curData=[this.index1,this.index2,this.index3,this.index4]
                    for (let i in indexData){
                      let curTd = document.getElementById(indexData[i])
                      curTd.style.color='#ffeb7b';
                      if(curData[i]<this.limit[i]){
                        curTd.style.color='rgb(222,49,59)';
                      }
                    }

                }, 2000);
                // 通过$once来监听定时器，在beforeDestroy钩子可以被清除
                this.$once('hook:beforeDestroy', () => {
                    clearInterval(this.timer);
                })

                window.addEventListener("resize", function() {
                    myChart.resize();
                });

            },

        }
    }
</script>

<style lang="scss" scoped>
    .chart {
        height: 100%;
        width: 100%;
        /*margin-top:10%!*10px*!;*/
        display: inline-block;
        /*border: 1px solid yellow;*/
    }
    .map-container {
        position: absolute;
        left: 0px;
        bottom: 0%;
        width: 100%;
        height: 100%;
        z-index: 2;
        /*border: 1px solid yellow;*/
    }
    .chart_box{
        width: 100%;
        height: 100%;  //注意，我这里举例子高度用的300px,如果想要高度也自适应这里可以用百分比
        /*display: inline-block;*/
        /*line-height: 500px;*/
        /*border: 1px solid red;*/
    }
    .mapPanel{
        color:#ade3ff;
        z-index:10;
        display: inline-block;
        position: fixed;
        border: 2px solid rgba(15, 79, 153, 0.8);
        /*background: rgba(255, 255, 255, 0.04) url(../assets/img/screen_img/line.png);*/
        background-color: rgba(0, 20, 91, 0.8);

        top:52%;
        /*left:69.5%;*/
        left: 0%;
        /*top:300px;*/
        /*left:766px;*/
        /*height: 26.5%;*/
        width: 30%;
        table{
            width: 100%;
            height: 100%;
        }
        td{
            /*width: 45%;*/
            text-align:center;
        }
        .bank_name{
            font-size: 18px;
            padding: 8px;
            span{
                color: #ffeb7b;
            }
        }
        .title{
            padding:5px 8px 0px 8px;
            background-color: rgba(15, 79, 153, 0.8);
        }
        .index{
            color: #ffeb7b;
            font-family: 'DIGITALDREAMFAT';
        }
        .long{
            padding: 0px;
        }
    }

</style>
