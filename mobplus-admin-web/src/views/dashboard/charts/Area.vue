<template>
    <div :style="{ padding: '0 0 32px 32px' }">
        <h4 :style="{ marginBottom: '20px' }">{{ title }}</h4>
        <v-chart height="254" :scale="scale" :data="data" :forceFit="true" :padding="['auto', 'auto', 'auto', 'auto']" >
            <v-tooltip />
            <template v-for="(item) in scale">
                <v-axis :dataKey="item.dataKey" :title="label" :label="label" :line="line"/>
                <v-smooth-line :position="position('time', item)" :color="item.color" :size="2" />
                <v-smooth-area :position="position('time', item)" :color="item.color"/>
            </template>
        </v-chart>
    </div>
</template>

<script>
export default {
    props: {
        title: {
            type: String,
            default: ''
        },
        data: {
            type: Array,
            default: () => {
                return [];
            }
        },
        scale: {
            type: Array,
            default: () => {}
        },
        tooltip: {
            type: Array,
            default: () => { }
        }
    },
    data () {
        return {
            label: {
                textStyle: { fill: '#000000D8' }
            },
            line: {
                lineWidth: 1 // 设置线的宽度
            }
        };
    },
    methods: {
        position: function (xaxis, scale) {
            return xaxis + '*' + scale.dataKey;
        }
    }
};
</script>
