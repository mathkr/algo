package de.algo.model;

import java.awt.*;

public class Selection {
        public Vector3 topL;
        public Vector3 botR;

        public Selection(Vector3 topL, Vector3 botR) {
                this.topL = topL;
                this.botR = botR;
        }

        public boolean contains(Vector3 p) {
                return     p.x >= topL.x && p.x <= botR.x
                        && p.y >= topL.y && p.y <= botR.y;
        }

        public void setSelection(Vector3 topL, Vector3 botR) {
                this.topL = topL;
                this.botR = botR;
        }
}
