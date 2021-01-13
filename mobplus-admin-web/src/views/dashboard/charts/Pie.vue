<template>
    <div :style="{ padding: '0 0 32px 32px' }">
        <h4 :style="{ marginBottom: '20px' }">{{ title }}</h4>
        <v-chart
            :force-fit="true"
            :height="405"
            :data="data"
            :scale="scale"
        >
            <v-tooltip :showTitle="false" dataKey="item*percent" />
            <v-axis />
            <v-legend data-key="item" />
            <v-pie position="percent" color="item" :vStyle="pieStyle" />
            <v-coord type="theta" :radius="0.75" :innerRadius="0.6" />
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
        }
    },
    data () {
        return {
            pieStyle: {
                stroke: '#fff',
                lineWidth: 1
            }
        };
    }
};
</script>
