app.controller("searchController", function ($scope, $location,searchService) {

    // 搜索对象
    $scope.searchMap = {"keywords":"", "category":"", "brand":"", "spec":{},"price":"","pageNo":1,"pageSize":20,"sortField":"","sort":""};

    //添加过滤条件
    $scope.search = function () {
        searchService.search($scope.searchMap).success(function (response) {
            $scope.resultMap = response;
            //构建分页导航条
            buildPageInfo();
        });

    };

    //添加过滤条件
    $scope.addSearchItem = function(key,value){
        if("brand" == key || "category" == key ||"price" == key){
            //如果点击的是品牌或者分类 显示面包屑
            $scope.searchMap[key]=value;//相当于为searchMap.brand或category赋值
        }else {
            //规格
            $scope.searchMap.spec[key]=value;
        }
        //点击过滤条件后需要重新搜索
        $scope.search();
    };

    // 删除过滤条件
    $scope.removeSearchItem = function (key) {
        if ("brand" == key || "category" == key || "price" == key) {
            //如果点击品牌或分类 就隐藏面包屑 显示为空
            $scope.searchMap[key]="";
        }else {
            //规格
            delete $scope.searchMap.spec[key];
        }
        //点击过滤条件后需要重新搜索
        $scope.search();
    };

    //构建分页导航条
    buildPageInfo = function () {

        //要分页导航条中显示的页号集合
        $scope.pageNoList = [];

        //要在导航条中显示的总页号个数
        var showPageCount = 5;

        //起始页号
        var startPageNo = 1;
        //结束页号
        var endPageNo = $scope.resultMap.totalPages;

        if($scope.resultMap.totalPages > showPageCount){

            //当前页应该间隔页数
            var interval = Math.floor((showPageCount/2));

            startPageNo = parseInt($scope.searchMap.pageNo) - interval;
            endPageNo = parseInt($scope.searchMap.pageNo) + interval;

            if(startPageNo > 0){
                //起始页号是正确的，但是结束页号需要再次判断
                if(endPageNo > $scope.resultMap.totalPages){
                    startPageNo = startPageNo - (endPageNo - $scope.resultMap.totalPages);
                    endPageNo = $scope.resultMap.totalPages;
                }
            } else {
                //起始页号已经出现问题（小于1）
                //endPageNo = endPageNo - (startPageNo -1);
                endPageNo = showPageCount;
                startPageNo = 1;
            }
        }

        //导航条中的前面3个点
        $scope.frontDot = false;
        if(startPageNo > 1){
            $scope.frontDot = true;
        }

        //导航条中的后面3个点
        $scope.backDot = false;
        if(endPageNo < $scope.resultMap.totalPages){
            $scope.backDot = true;
        }


        for (var i = startPageNo; i <= endPageNo; i++) {
            $scope.pageNoList.push(i);
        }
    };


        //判断是否为当前页
        $scope.isCurrentPage = function (pageNo) {
            return $scope.searchMap.pageNo == pageNo ;
        };

        //根据页号查询
        $scope.queryByPageNo = function (pageNo) {
            if (0 < pageNo && pageNo <= $scope.resultMap.totalPages){
                $scope.searchMap.pageNo = pageNo;
                $scope.search();
            }
        };

        //添加排序
        $scope.addSortField = function (sortField, sort) {
            $scope.searchMap.sortField = sortField;
            $scope.searchMap.sort = sort;

            $scope.search();
        };

        //加载搜索关键字
        $scope.loadKeywords = function () {
            $scope.searchMap.keywords = $location.search()["keywords"];
            $scope.search();
        };

});