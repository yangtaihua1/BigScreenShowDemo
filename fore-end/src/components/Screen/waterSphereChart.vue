<template>
  <div class="waterSphereChart-container">
    <div class="chart" id="chart_middel1"></div>
  </div>
</template>

<script>
import echarts from "echarts";
import 'echarts-liquidfill';
export default {
  name: "waterSphereChart",
  data(){
    return{
      value:this.risk
    }
  },
  props:{
    risk:{
      type:Number,
      default:0.1
    }
  },
  computed: {
  },
  watch:{//监听value变化
    risk:{
      handler(newVal,oldVal) {// eslint-disable-line no-unused-vars
        console.log("watch: map store更改！！")
        this.value=newVal;
        this.getChart();
      }
    }
  },
  // mounted() {
  //   this.getChart();
  // },
  methods: {
    getChart(){
      var myChart = echarts.init(document.getElementById('chart_middel1'))
      var data = [this.value, this.value-0.1, this.value-0.2,]
      var option ={
        series: [{
          type: 'liquidFill',
          radius: '80%',
          data: data,
          animationDuration: 0, //初始动画持续时间，即底部升起时间
          animationDurationUpdate: 0, //其他动画持续时间
          animationEasingUpdate: "cubicOut", //初始动画的缓和方法，底部升起
          backgroundStyle: {
            color: {
              type: 'linear',
              x: 1,
              y: 0,
              x2: 0.5,
              y2: 1,
              colorStops: [{
                offset: 1,
                color: 'rgba(68, 145, 253, 0)'
              }, {
                offset: 0.5,
                color: 'rgba(68, 145, 253, .25)'
              }, {
                offset: 0,
                color: 'rgba(68, 145, 253, 1)'
              }],
              globalCoord: false
            },
            borderColor: "rgba(243,43,45,0.8)",
            borderWidth: 2,//边框粗细
          },
          //波浪样式
          itemStyle: {
            color: {
              type: "linear",
              x: 0,
              y: 0,
              x2: 0,
              y2: 1,
              colorStops: [
                {
                  offset: 1,
                  // color: "rgba(237,55,55, 0.2)"
                  color:"rgba(243,43,45,0.4)"
                },
                {
                  offset: 0.5,
                  // color: "rgba(247,117,103, 0.6)"
                  color:"rgba(243,43,45,0.7)"
                },
                {
                  offset: 0,
                  // color: "rgba(199,84,80,1)"
                  color:"rgba(243,43,45,1)"
                }
              ],
              globalCoord: false
            },
          },
          label: {
            normal: {
              textStyle: {
                color: 'rgb(230,108,0,1)',
                insideColor: 'yellow',
                fontSize: 13
              }
            }
          },
          outline: {
            // show:false,
            borderDistance: 0,
            itemStyle: {
              borderWidth:2,
              shadowBlur: 4,
              shadowColor:"#fff"
            }
          },
        }]
      };
      var option2 = {
        series: [
          {
            type: "liquidFill",
            radius: "80%",
            data: data,
            animationDuration: 0,
            animationDurationUpdate: 0,
            animationEasingUpdate: "cubicOut",
            backgroundStyle: {
              color: {
                type: "linear",
                x: 1,
                y: 0,
                x2: 0.5,
                y2: 1,
                colorStops: [
                  {
                    offset: 1,
                    color: "rgba(68, 145, 253, 0)"
                  },
                  {
                    offset: 0.5,
                    color: "rgba(68, 145, 253, .25)"
                  },
                  {
                    offset: 0,
                    color: "rgba(68, 145, 253, 1)"
                  }
                ],
                globalCoord: false
              },
              borderColor: "#007bff", //边框颜色
              borderWidth: 2, //边框粗细
              shadowColor: "rgba(0, 123, 225, 0.4)",
              shadowBlur: 20
            },
            //波浪样式
            itemStyle: {
              color: {
                type: "linear",
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  {
                    offset: 1,
                    color: "rgba(58, 71, 212, 0)"
                  },
                  {
                    offset: 0.5,
                    color: "rgba(31, 222, 225, .2)"
                  },
                  {
                    offset: 0,
                    color: "rgba(31, 222, 225, 1)"
                  }
                ],
                globalCoord: false
              }
            },
            label: {
              normal: {
                textStyle: {
                  color: "rgb(46,200,207,1)",
                  insideColor: "yellow",
                  fontSize: 13
                }
              }
            },
            outline: {
              show: false
            }
          }
        ]
      };
      myChart.clear();
      if (this.value > 0) {
        myChart.setOption(option);
        window.addEventListener("resize", function() {
          myChart.resize();
        });
      } else {
        myChart.setOption(option2);
        window.addEventListener("resize", function() {
          myChart.resize();
        });
      }
    }
  }
}
</script>

<style lang="scss" scoped>
  .chart {
    width: 100%;
    height: 100%;
  }
  .waterSphereChart-container {
    position: absolute;
    overflow: hidden;
    display: block;
    cursor: default;
    z-index: 0;
    /*left: 510px;*/
    /*top: 9.5%;*/
    width: 100%;
    height: 100%;
  }
</style>
