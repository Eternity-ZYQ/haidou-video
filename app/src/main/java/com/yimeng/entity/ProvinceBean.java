package com.yimeng.entity;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/1/8 0008 下午 06:25.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class ProvinceBean implements IPickerViewData {

    /**
     * id : 2
     * cityName : 北京市
     * citys : [{"id":3,"cityName":"北京市","districts":[{"id":4,"cityName":"东城区"},{"id":5,"cityName":"西城区"},{"id":6,"cityName":"朝阳区"},{"id":7,"cityName":"丰台区"},{"id":8,"cityName":"石景山区"},{"id":9,"cityName":"海淀区"},{"id":10,"cityName":"门头沟区"},{"id":11,"cityName":"房山区"},{"id":12,"cityName":"通州区"},{"id":13,"cityName":"顺义区"},{"id":14,"cityName":"昌平区"},{"id":15,"cityName":"大兴区"},{"id":16,"cityName":"怀柔区"},{"id":17,"cityName":"平谷区"},{"id":18,"cityName":"密云区"},{"id":19,"cityName":"延庆区"},{"id":20,"cityName":"中关村科技园区"}]}]
     */

    private int id;
    private String cityName;
    private List<CitysBean> citys;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    // 实现 IPickerViewData 接口，
    // 这个用来显示在PickerView上面的字符串，
    // PickerView会通过IPickerViewData获取getPickerViewText方法显示出来。
    @Override
    public String getPickerViewText() {
        return this.cityName;
    }

    public List<CitysBean> getCitys() {
        return citys;
    }

    public void setCitys(List<CitysBean> citys) {
        this.citys = citys;
    }

    public static class CitysBean implements IPickerViewData{
        /**
         * id : 3
         * cityName : 北京市
         * districts : [{"id":4,"cityName":"东城区"},{"id":5,"cityName":"西城区"},{"id":6,"cityName":"朝阳区"},{"id":7,"cityName":"丰台区"},{"id":8,"cityName":"石景山区"},{"id":9,"cityName":"海淀区"},{"id":10,"cityName":"门头沟区"},{"id":11,"cityName":"房山区"},{"id":12,"cityName":"通州区"},{"id":13,"cityName":"顺义区"},{"id":14,"cityName":"昌平区"},{"id":15,"cityName":"大兴区"},{"id":16,"cityName":"怀柔区"},{"id":17,"cityName":"平谷区"},{"id":18,"cityName":"密云区"},{"id":19,"cityName":"延庆区"},{"id":20,"cityName":"中关村科技园区"}]
         */

        private int id;
        private String cityName;
        private List<DistrictsBean> districts;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        @Override
        public String getPickerViewText() {
            return this.cityName;
        }

        public List<DistrictsBean> getDistricts() {
            return districts;
        }

        public void setDistricts(List<DistrictsBean> districts) {
            this.districts = districts;
        }

        public static class DistrictsBean implements IPickerViewData{
            /**
             * id : 4
             * cityName : 东城区
             */

            private int id;
            private String cityName;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCityName() {
                return cityName;
            }

            public void setCityName(String cityName) {
                this.cityName = cityName;
            }

            @Override
            public String getPickerViewText() {
                return this.cityName;
            }
        }
    }
}
