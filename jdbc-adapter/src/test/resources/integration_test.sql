CREATE TABLE NILS_TRANSLATION(
  ID INT PRIMARY KEY,  
  NLS_LOCALE VARCHAR(10) NOT NULL,
  NLS_KEY VARCHAR(255) NOT NULL,
  NLS_VALUE VARCHAR(255) NOT NULL
);
INSERT INTO NILS_TRANSLATION VALUES(1, '', 'simple', 'A simple translation');
INSERT INTO NILS_TRANSLATION VALUES(2, '', 'fallback', 'A fallback translation');
INSERT INTO NILS_TRANSLATION VALUES(3, '', 'with_args', 'A {0} with {1}.');
INSERT INTO NILS_TRANSLATION VALUES(4, '', 'Dummy.attribute', 'Attribute');
INSERT INTO NILS_TRANSLATION VALUES(5, '', 'data.BaseMessage.buttons.ok', 'OK (BaseMessage)');
INSERT INTO NILS_TRANSLATION VALUES(6, '', 'data.BaseMessage.buttons.cancel', 'Cancel (BaseMessage)');
INSERT INTO NILS_TRANSLATION VALUES(7, '', 'data.Message._include', 'data.BaseMessage');
INSERT INTO NILS_TRANSLATION VALUES(8, '', 'data.Message.INCLUDE', 'data.AlternativeMessage');
INSERT INTO NILS_TRANSLATION VALUES(9, '', 'data.Message.buttons.cancel', 'Cancel (Message)');
INSERT INTO NILS_TRANSLATION VALUES(10, '', 'data.AlternativeMessage.buttons.ok', 'Ok (AlternativeMessage)');
INSERT INTO NILS_TRANSLATION VALUES(11, '', 'Level0.value', 'Value (Level0)');
INSERT INTO NILS_TRANSLATION VALUES(12, '', 'Level1._include', 'Level0');
INSERT INTO NILS_TRANSLATION VALUES(13, '', 'Level2._include', 'Level1');
INSERT INTO NILS_TRANSLATION VALUES(14, '', 'Cycle1._include', 'Cycle2');
INSERT INTO NILS_TRANSLATION VALUES(15, '', 'Cycle2._include', 'Cycle1');
INSERT INTO NILS_TRANSLATION VALUES(16, 'de', 'simple', 'Eine einfache Übersetzung');