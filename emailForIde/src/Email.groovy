class Email {

    private String _from;
    public void from(final String val) { _from = val; }

    private String _to;
    public void to(final String val) { _to = val; }

    private String _subject;
    public void subject(final String val) { _subject = val; }

    private String _body;
    public void body(final String val) { _body = val; };

    @Override String toString() {
        return """
To: ${_to}
From: ${_from}
Subject: ${_subject}
Body: ${_body}
""";
    }

    private void send() {
        Thread.start {
            int wait = new Random().nextInt(6);
            sleep(wait * 1_000);
            println("Verified in ${wait} seconds");
            println("Sending: ${this}");
        }
    }

    public static void email(@DelegatesTo(Email) final Closure closure) {
        Email e = new Email();
        closure.setDelegate(e);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.call();
        e.send();
    }
}
