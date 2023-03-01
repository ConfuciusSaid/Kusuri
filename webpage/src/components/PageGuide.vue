<template>
  <div v-if="page&&page.records.length!==0" class="wrapper-guide">
    <!--    <div v-show="page.current>2" class="btn-page" @click="onFlip(1)">&lt;&lt;</div>-->
    <div :class="page.current>1?'btn-page':'btn-disable'" @click="onFlip(page.current-1)">◀</div>
    <div v-if="page" :class="(start+index)===page.current?'btn-current':'btn-page'" v-for="(reserve,index) in num">
      <div @click="onFlip(start + index)">{{ start + index }}</div>
    </div>
    <div :class="page.pages-page.current>0?'btn-page':'btn-disable'" @click="onFlip(page.current+1)">▶</div>
    <!--    <div v-show="page.pages-page.current>1" class="btn-page" @click="onFlip(page.pages)">>></div>-->

  </div>
  <div v-else-if="page&&page.records.length===0" class="empty-text">
    空空如也
  </div>
</template>

<script>
export default {
  name: "PageGuide",
  props: ['page', 'flip'],
  setup($props) {
    let page = $props.page;
    let start;
    let num;
    if (page) {
      if (page.pages > 5) {
        start = (page.current < 3 ?
            1 : (page.pages - page.current > 1 ?
                page.current - 2 : page.pages - 4));
        num = 5;
      } else {
        start = 1;
        num = page.pages;
      }
    }

    function onFlip(pageNo) {
      if (pageNo !== page.current && pageNo >= 1 && pageNo <= page.pages) {
        page.current = pageNo;
        $props.flip(pageNo);
      }
    }

    return {
      start,
      num,
      onFlip
    }
  }
}
</script>

<style scoped>
.wrapper-guide {
  margin-top: 25px;
  display: flex;
  align-self: center;
}

.btn-page, .btn-current, .btn-disable {
  width: 30px;
  height: 30px;
  text-align: center;
  box-shadow: 0 0 3px gray;
  border-radius: 5px;

  display: flex;
  flex-direction: column;
  justify-content: center;

  margin: 0 10px 0;
  cursor: pointer;

  transition: .2s;

  user-select: none;
}

.btn-current {
  color: white;
  background-color: dodgerblue;
  cursor: default;
}

.btn-disable {
  color: #949494;
  background-color: #e0e0e0;
  cursor: default;
}

.empty-text{
  text-align: center;
  color: gray;
  margin-top: 25px;
  user-select: none;
}

@media (max-width: 719px) {
  .btn-page, .btn-current, .btn-disable {
    margin: 0 6px 0;
  }
}

</style>
