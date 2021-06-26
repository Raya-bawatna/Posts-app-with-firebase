package raya.cs.birzeit.simpleblogapp;

public class CommentModel {
    private String Comment;
    private String publisher;

    public CommentModel(String Comment,String publisher){
        this.Comment = Comment;
        this.publisher = publisher;

    }

    public CommentModel() {
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Override
    public String toString() {
        return "CommentModel{" +
                "comment='" + Comment + '\'' +
                ", publisher='" + publisher + '\'' +
                '}';
    }



}
