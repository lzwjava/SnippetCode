package com.avoscloud.chat;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVRelation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.List;

public class RelationGuideSnippet2 {
  public void pointerOneToOneSave() {
    AVObject post = new AVObject("Post");
    post.put("createdBy", AVUser.getCurrentUser());
  }

  void pointerOneToOneQuery() {
    AVQuery postQuery = new AVQuery("Post");
    postQuery.whereEqualTo("createdBy", AVUser.getCurrentUser());
  }

  void pointerOneToOneGet() {
    AVObject post;

    AVUser createdBy = post.getAVUser("createdBy");
  }

  void avobjectArraySave() {
    // 假设我们有四件商品
    AVObject coffee;
    AVObject chip;
    AVObject beer;
    AVObject cookie;

    // 将商品保存在数组中
    List<AVObject> products = new ArrayList<>();
    products.add(coffee);
    products.add(chip);
    products.add(beer);
    products.add(coffee);

    // 将商品保存在用户的购物车中
    AVUser.getCurrentUser().put("cartProducts", products);
  }

  void avobjectArrayGet() {
    List<AVObject> products = AVUser.getCurrentUser().getList("cartProducts");
  }

  void avobjectArrayQueryinclude() {
    // 从 AVUser 对象获得 AVQuery 对象
    AVQuery<AVUser> userQuery = AVUser.getQuery();

    // 为查询设置约束
    // 比如，你想查询最近一个小时登录过的用户

    // 让这个查询得到用户的同时，得到他们购物车上的商品列表
    userQuery.include("cartProducts");

    // 执行查询
    userQuery.findInBackground(new FindCallback<AVUser>() {
      @Override
      public void done(List<AVUser> list, AVException e) {
        // objects 包含了所有满足条件的用户，和与之关联购物车商品列表
      }
    });
  }

  void avobjectArrayQueryContainedIn() {
    AVObject coffee ;
    AVQuery<AVUser> userQuery ;
    List arrayOfProducts;

    // 加上约束，查询购物车里有某个特定的商品的用户
    userQuery.whereEqualTo("cartProducts", coffee);

    // 或者查询购物车中包含了几个商品的用户
    userQuery.whereContainedIn("cartProduts", arrayOfProducts);
  }

  void avrelationSave() {
    // 假如我们用下面的对象来表示几个歌手
    AVObject artistOne;
    AVObject artistTwo;
    AVObject artistThree;

    // 创建一条歌曲记录
    AVObject song = new AVObject("Song");

    // 我们把歌手和歌曲关联起来，在 Song 对象中创建一个 "artists" Relation
    AVRelation relation = song.getRelation("artists");
    // 请确保这些对象在关联之前已经保存到了服务器上
    relation.add(artistOne);
    relation.add(artistTwo);
    relation.add(artistThree);

    // 保存 Song 对象
    song.saveInBackground();
  }

  void avrelationQuery() {
    // 假如有一个 song 对象
    AVObject song;

    // 在 artists 字段上得到一个 relation
    AVRelation relation = song.getRelation("artists");

    // 根据上面的 relation 得到一个 AVQuery 对象
    AVQuery query = relation.getQuery();

    // 执行查询
  }


  void avrelationQueryEqualTo() {
    // 假如我们有一个 artist 对象，希望获得该 artist 创作的所有歌曲
    AVObject artist;

    // 首先，对 Song 对象创建一个查询
    AVQuery query = new AVQuery("Song");

    // 我们查询所有的 Song，看哪些 Song 的 artists 关联包含了特定的 artist
    query.whereEqualTo("artists", artist);
  }

  void relatedClassSave() {
    // 假定我们有一个即将要加入的班级
    AVObject cls;

    // 在表中创建一条记录
    AVObject memberRelation = new AVObject("MemberRelation");
    memberRelation.put("class", cls);
    memberRelation.put("member", AVUser.getCurrentUser());
    memberRelation.put("role", "leader");
    memberRelation.saveInBackground();
  }

  void relatedClassQueryFrom() {

    // 对 MemberRelation 表创建一个查询
    AVQuery<AVObject> query = new AVQuery("MemberRelation");
    query.whereEqualTo("member", AVUser.getCurrentUser())

    // 执行查询
    query.findInBackground(new FindCallback<AVObject>() {
      @Override
      public void done(List<AVObject> list, AVException e) {
        for (AVObject o : list) {
          // o 是 MemberRelation 表的一条记录
          // 获取当前用户所在班级
          AVObject cls = o.getAVObject("class");

          // 获取当前用户在班级里的角色
          String role = o.getString("role");
        }
      }
    });
  }

  void relatedClassQueryTo() {
    AVObject cls;

    // 对 MemberRelation 表创建一个查询
    AVQuery<AVObject> query = new AVQuery("MemberRelation");
    query.whereEqualTo("class", cls);
    query.findInBackground(new FindCallback<AVObject>() {
      @Override
      public void done(List<AVObject> list, AVException e) {
        for (AVObject o : list) {
          // o 是 MemberRelation 表的一条记录
          // 获取相应的班级成员
          AVUser member = o.getAVUser("member");

          // 获取该成员在班级里的角色
          String role = o.getString("role");
        }
      }
    });
  }

  void avobjectArrayManyToManySave() {
    // 假设我们有一个歌手
    AVObject artist;

    // 假设我们也有一首歌曲
    AVObject song;

    // 把相应的歌手加到 song 的 artists 数组中
    song.add("artists", artist);
  }

  void avobjectArrayManyToManyQuery() {
    // 对 Song 表创建一个查询
    AVQuery<AVObject> songQuery = new AVQuery("Song");

    // 设置约束
    // 让查询同时返回每个 Song 中的 artists 列表
    songQuery.include("artists");

    // 执行查询
    songQuery.findInBackground(new FindCallback<AVObject>() {
      @Override
      public void done(List<AVObject> list, AVException e) {
        // list 是所有 Song 对象，同时包含了关联的 Artist 对象
      }
    });
  }

  void avobjectArrayManyToManyGet() {
    AVObject song;

    List<AVObject> artistList = song.getList("artists");
  }

  void avobjectArrayManyToManyQueryEqualTo() {
    // 假如我们有一个 artist 对象
    AVObject artist;

    // 对 Song 表创建一个查询
    AVQuery<AVObject> songQuery = new AVQuery("Song");

    // 约束查询
    songQuery.whereEqualTo("artists", artist);

    // 让查询同时返回 artists 列表
    songQuery.include("artists");

    // 执行查询
    songQuery.findInBackground(new FindCallback<AVObject>() {
      @Override
      public void done(List<AVObject> list, AVException e) {
        // list 是所有 Song 对象，同时包含了关联的 Artist 对象
      }
    });
  }

}